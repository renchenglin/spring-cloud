package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Resource
    private UserRepository userRepository;
	
	public Page<User> find(User user, Integer page, Integer size, Sort sort) {
		ExampleMatcher matcher = ExampleMatcher.matching()
	            .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.contains())
	            .withMatcher("userName" ,ExampleMatcher.GenericPropertyMatchers.contains())
	            .withMatcher("address", ExampleMatcher.GenericPropertyMatchers.contains())
	            .withMatcher("homeAddress" ,ExampleMatcher.GenericPropertyMatchers.contains())
	            .withMatcher("homeTel" ,ExampleMatcher.GenericPropertyMatchers.contains())
//	            .withIgnorePaths("age")
	            .withIgnorePaths("id");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
	    Example<User> example = Example.of(user ,matcher);
		
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<User> pageUser = userRepository.findAll(example, pageable);
		return pageUser;
	}

	
	/***
	 * Specification 的方式，可以支持复杂的查询条件，如 or的
	 * @param user
	 * @param page
	 * @param size
	 * @param sort
	 * @return
	 */
	@SuppressWarnings("serial")
	public Page<User> findAnother(User user, Integer page, Integer size, Sort sort) {
		Specification<User> querySpecification = new Specification<User>() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(user.getUserId())) {
                    predicates.add(criteriaBuilder
                            .like(root.get("userId"), "%" + user.getUserId() + "%"));
                }
                if(StringUtils.isNotEmpty(user.getAddress())){
                    predicates.add(criteriaBuilder
                            .like(root.get("address"), "%" + user.getAddress() + "%"));
                }
                if(StringUtils.isNotEmpty(user.getUserName())){
                	predicates.add(criteriaBuilder.like(root.get("userName"), "%" + user.getUserName() + "%"));
                }
                if(StringUtils.isNotEmpty(user.getHomeAddress())){
                    predicates.add(criteriaBuilder
                            .like(root.get("homeAddress"), "%" + user.getHomeAddress() + "%"));
                }
                if(ObjectUtils.isNotEmpty(user.getHomeTel())){
                    predicates.add(criteriaBuilder.like(root.get("homeTel"), "%" + user.getHomeTel() + "%"));
                }
                if(ObjectUtils.isNotEmpty(user.getAge())){
                    predicates.add(criteriaBuilder.equal(root.get("age"),  user.getAge()));
                }
                
//				  组装一个or的表达式                
//                Predicate p1 = criteriaBuilder.like(root.get("address"),"zt%");
//                Predicate p2 = criteriaBuilder.like(root.get("name"),"foo%");
//                Predicate p3 = criteriaBuilder.or(p1,p2);
                
//                if (StringUtils.isNotBlank(searchArticle.getNickname())){
//                    //两张表关联查询
//                    Join<Article,User> userJoin = root.join(root.getModel().getSingularAttribute("user",User.class),JoinType.LEFT);
//                    predicate.add(cb.like(userJoin.get("nickname").as(String.class), "%" + searchArticle.getNickname() + "%"));
//                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        PageRequest pageRequest = PageRequest.of(page, size, sort);

		Page<User> pageUser = userRepository.findAll(querySpecification, pageRequest);
		return pageUser;
	}
	
	public Page<User> findTwo(String whereSqlCondition, Integer page, Integer size, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);

		Page<User> pageUser = userRepository.findUserBySql(whereSqlCondition, pageRequest);
		return pageUser;
	}
}
