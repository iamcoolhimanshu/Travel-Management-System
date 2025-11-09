package com.codeWithHimanshu.Travel.repository;

import com.codeWithHimanshu.Travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long> {

}
