package org.duckdns.petfinderapp.domain.post.repository;

import java.util.List;

import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostCommon, Long>,
    JpaSpecificationExecutor<PostCommon> {

  List<PostCommon> findAllByOrderByIdDesc();

  @Query(
      value = "select p from PostCommon p"
          + " where treat(p as Adopt).petNum = :petNum"
          + " or treat(p as Lost).petNum = :petNum",
      countQuery = "select count(p) from PostCommon p"
          + " where treat(p as Adopt).petNum = :petNum"
          + " or treat(p as Lost).petNum = :petNum")
  Page<PostCommon> findAdoptOrLostByPetNum(String petNum, Pageable pageable);

  Page<PostCommon> findAllByUser(User user, Pageable pageable);
}
