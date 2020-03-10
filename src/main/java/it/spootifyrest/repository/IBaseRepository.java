package it.spootifyrest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface IBaseRepository<T> extends CrudRepository<T, Long>, QueryByExampleExecutor<T> {

}
