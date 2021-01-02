/**
 * 
 */
package com.seeth.globalsearch.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.seeth.globalsearch.models.FacetFilter;
import com.seeth.globalsearch.models.GlobalSearchModel;
import com.seeth.globalsearch.models.OperationType;
import com.seeth.globalsearch.models.SearchCriteria;
import com.seeth.globalsearch.models.EntitySearchModel.SearchMeta;
import com.seeth.globalsearch.specifications.RelationSpecification;
import com.seeth.user.personal.models.Role;
import com.seeth.utils.models.status.UserStatusUtility.Gender;
import com.seeth.utils.models.status.UserStatusUtility.MaritalStatus;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.seeth.utils.models.status.UserStatusUtility.VisaStatus;
import com.tata.user.personal.models.User_;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SeethendReddy
 *
 */
@Slf4j
public class EntitySearchHelperService<T> {

	@SuppressWarnings("unchecked")
	public static <T> Specification<T> getAllSpecifications(HashMap<String, List<FacetFilter>> facets, Map<String, SearchMeta> entitySearchModel) {

		Set<Specification<T>> specifications = new HashSet<>();

		if(facets != null) {
			facets.entrySet()
			.stream()
			.forEach(
				(p -> {
					Specification<T> spec = (Specification<T>) getSpecification(p, entitySearchModel);
					if(spec != null) specifications.add(spec);
				})
			);
		}

		Specification<T> finalSpec = null;

		for (Specification<T> spec : specifications) {
			finalSpec = Specification.where(finalSpec).and(spec);
		}

		return finalSpec;
	}

	public static <T> Specification<T> getSpecification(Entry<String, List<FacetFilter>> p, Map<String, SearchMeta> entitySearchModel) {
		Map<Object, OperationType> returnFilterSet = new HashMap<>();
		Specification<T> returnPredicate = null;
		List<FacetFilter> facetFilters;
		try {
			SearchMeta fieldSearchMeta = entitySearchModel.get(p.getKey());
			if(fieldSearchMeta.isFilterEnable()) {
				facetFilters = p.getValue();
				try {
					facetFilters.stream().filter(FacetFilter::isEnabled).forEach(l->{
						Object value = l.getValue();

						value = getValueObject(fieldSearchMeta.getClassType(), value);
						
						returnFilterSet.put(value, l.getOperationType());
					});	

					returnPredicate = getPredicate(fieldSearchMeta.getAttribute(), fieldSearchMeta.getFieldName(), returnFilterSet);
				} catch(Exception e) {
					log.error("Invalid Field value or cannot parser the content !!!");
					return null;
				}
			}
		} catch(Exception e) {
			log.error("Invalid Field or cannot parser the content !!!");
			return null;
		}
		return returnPredicate;
	}

	private static Object getValueObject(Class<?> classType, Object value) {
		
		if(classType.equals(UserRole.class))
			value = UserRole.valueOf((String) value);
		else if(classType.equals(Gender.class))
			value = Gender.valueOf((String) value);
		else if(classType.equals(MaritalStatus.class))
			value = MaritalStatus.valueOf((String) value);
		else if(classType.equals(VisaStatus.class))
			value = VisaStatus.valueOf((String) value);
		else if(classType.equals(Integer.class))
			value = Integer.parseInt(value.toString());
		else if(classType.equals(Double.class))
			value = Double.parseDouble(value.toString());
		else
			value = (String) value;
		
		return value;
	}

	public static <T> Specification<T> getPredicate(String searchEntity, String filterName, Map<Object, OperationType> returnFilterSet) {
		Specification<T> returnSpec = null;
		for(Map.Entry<Object, OperationType> filter: returnFilterSet.entrySet()){

			OperationType operationType = OperationType.EQUALS_TO;

			if(filter.getValue() != null) {
				operationType = filter.getValue();
			}

//			GlobalSpecification<T> productSpecification = 
//					new GlobalSpecification<T>(new SearchCriteria(searchEntity, filterName, operationType, filter.getKey()));
			
			RelationSpecification<T, ?> spec = null;
			
			if(searchEntity != null) {
				if(searchEntity.equals(User_.ROLES)) {
					spec = new RelationSpecification<T, Role>(new SearchCriteria(searchEntity, filterName, operationType, filter.getKey()));
				}
			} else {
				spec = new RelationSpecification<T, Object>(new SearchCriteria(filterName, operationType, filter.getKey()));
			}

			returnSpec = Specification.where(returnSpec).or(spec);
		}
		return returnSpec;
	}

	public static Pageable getPageableOptions(GlobalSearchModel globalSearchModel,
			int pageNumber, int itemsPerPage, Map<String, SearchMeta> entitySearchModel) {

		if (globalSearchModel.getSortBy() != null) {

			Sort sort = Sort.by("createDate").descending();
			List<Order> allOrderBy = new ArrayList<>();

			List<FacetFilter> facetSortFilters = globalSearchModel.getSortBy();
			for (FacetFilter f : facetSortFilters) {
				if (f.isEnabled()) {
					if(entitySearchModel.containsKey(f.getName())) {
						SearchMeta fieldSearchMeta = entitySearchModel.get(f.getName());
						if(fieldSearchMeta.isSortEnable())
							allOrderBy.add(Sort.Order.desc(fieldSearchMeta.getFieldName()));
					}
				}
			}

			if(allOrderBy.size() > 0)
				sort = Sort.by(allOrderBy);

			return PageRequest.of(pageNumber, itemsPerPage, sort);
		} else{
			return PageRequest.of(pageNumber, itemsPerPage);
		}
	}

}
