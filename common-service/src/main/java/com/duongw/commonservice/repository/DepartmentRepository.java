package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.model.projection.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
        JpaSpecificationExecutor<Department> {

    Department findByDepartmentName(String name);

    Department findByDepartmentCode(String name);

    List<Department> findByStatus(Long status);

    @Query(value = """
            SELECT 
                d.department_id as id,
                d.department_name as name,
                d.department_code as code,
                d.parent_department_id as parentId,
                d.status as status,
                LEVEL as depth,
                CONNECT_BY_ISLEAF as isLeaf,
                CONNECT_BY_ROOT(d.department_name) as rootName,
                PRIOR d.department_name as parentName,
                PRIOR d.department_code as parentCode
            FROM department d
            START WITH d.parent_department_id IS NULL
            CONNECT BY PRIOR d.department_id = d.parent_department_id
            ORDER SIBLINGS BY d.department_name
            """, nativeQuery = true)
    List<DepartmentProjection> findDepartmentHierarchy();
}

