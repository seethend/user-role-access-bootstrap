/**
 * 
 */
package com.seeth.globalsearch.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.seeth.globalsearch.models.OperationType;
import com.seeth.globalsearch.models.SearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author seeth
 *
 */
@Data
@AllArgsConstructor
public class RelationSpecification<T, Y> implements Specification<T>{

	private static final long serialVersionUID = 2501069022494838068L;

	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		
		From<T, ?> superCriteria = root;
		
		if(criteria.getEntity() != null) {
			superCriteria = root.join(criteria.getEntity());
		}
        
		if (criteria.getOperationType().equals(OperationType.GREATER_THAN)) {
            
        	return builder.greaterThanOrEqualTo(
        			superCriteria.get(criteria.getKey()), criteria.getValue().toString());
        
        } else if (criteria.getOperationType().equals(OperationType.LESS_THAN)) {
            
        	return builder.lessThanOrEqualTo(
        			superCriteria.get(criteria.getKey()), criteria.getValue().toString());
        
        } else if (criteria.getOperationType().equals(OperationType.EQUALS_TO)) {
           
        	return builder.equal(
        			superCriteria.get(criteria.getKey()), criteria.getValue());
        
        } else if (criteria.getOperationType().equals(OperationType.LIKE))  {

        	return builder.like(
        			superCriteria.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
        
        return null;
	}

}
