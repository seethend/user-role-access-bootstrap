package com.seeth.globalsearch.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.jpa.domain.Specification;

import com.seeth.globalsearch.models.OperationType;
import com.seeth.globalsearch.models.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
@AllArgsConstructor
public class GlobalSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 8977427683734293910L;

	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperationType().equals(OperationType.GREATER_THAN)) {
            
        	return builder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        
        } else if (criteria.getOperationType().equals(OperationType.LESS_THAN)) {
            
        	return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        
        } else if (criteria.getOperationType().equals(OperationType.EQUALS_TO)) {
           
        	return builder.equal(
        			root.get(criteria.getKey()), criteria.getValue());
        
        } else if (criteria.getOperationType().equals(OperationType.LIKE))  {

        	return builder.like(
                    root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
       
        }
        //custom logic for further updates
//        else if (criteria.getOperation().equalsIgnoreCase(OperationType.NOT_EQUALS_TO.toString())) {
//            return builder.between(
//                    root. get(criteria.getKey()), (BigDecimal)(criteria.getValue()),(BigDecimal) criteria.getMaxVal());
//        }
        return null;
	}
	
}
