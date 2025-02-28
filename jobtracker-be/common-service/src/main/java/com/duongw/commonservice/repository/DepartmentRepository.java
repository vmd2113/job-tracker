package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.model.projection.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
        JpaSpecificationExecutor<Department> {

    Department findByDepartmentName(String name);

    Department findByDepartmentCode(String name);

    List<Department> findByStatus(Long status);

    // 1. Oracle CONNECT BY – Full Hierarchy
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

    // 2. Recursive CTE – Full Hierarchy
    @Query(value = """
            WITH DepartmentCTE (department_id, department_name, department_code, parent_department_id, status, depth, parent_name, parent_code) AS (
                SELECT department_id, department_name, department_code, parent_department_id, status, 1 as depth, NULL as parent_name, NULL as parent_code
                FROM department
                WHERE parent_department_id IS NULL
                UNION ALL
                SELECT d.department_id, d.department_name, d.department_code, d.parent_department_id, d.status, c.depth + 1, c.department_name, c.department_code
                FROM department d
                INNER JOIN DepartmentCTE c ON d.parent_department_id = c.department_id
            )
            SELECT department_id as id,
                   department_name as name,
                   department_code as code,
                   parent_department_id as parentId,
                   status,
                   depth,
                   0 as isLeaf,
                   parent_name as parentName,
                   parent_code as parentCode
            FROM DepartmentCTE
            ORDER BY department_name
            """, nativeQuery = true)
    List<DepartmentProjection> findDepartmentHierarchyCTE();

    // 3. Oracle CONNECT BY – Direct Children của 1 node
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
            START WITH d.department_id = ?1
            CONNECT BY PRIOR d.department_id = d.parent_department_id
            AND LEVEL = 2
            """, nativeQuery = true)
    List<DepartmentProjection> findDepartmentTreeByIdConnectBy(Long nodeId);

    // 4. Recursive CTE – Direct Children của 1 node
    @Query(value = """
            WITH DirectChildrenCTE AS (
                SELECT department_id, department_name, department_code, parent_department_id, status, 1 as depth
                FROM department
                WHERE department_id = :nodeId
                UNION ALL
                SELECT d.department_id, d.department_name, d.department_code, d.parent_department_id, d.status, c.depth + 1
                FROM department d
                INNER JOIN DirectChildrenCTE c ON d.parent_department_id = c.department_id
                WHERE c.depth = 1
            )
            SELECT department_id as id,
                   department_name as name,
                   department_code as code,
                   parent_department_id as parentId,
                   status,
                   depth,
                   0 as isLeaf,
                   NULL as parentName,
                   NULL as parentCode
            FROM DirectChildrenCTE
            WHERE depth = 2
            """, nativeQuery = true)
    List<DepartmentProjection> findDepartmentChildrenCTE(@Param("nodeId") Long nodeId);

    List<Department> findByParentDepartment_DepartmentId(Long parentId);
}
