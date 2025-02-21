import PropTypes from "prop-types";
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";
import React from "react";

const DepartmentList = ({departments, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {
    const isAllSelected = departments.length > 0 && departments.every(department => selectedIds.has(department.departmentId));
    const isIndeterminate = departments.length > 0 &&
        departments.some(department => selectedIds.has(department.departmentId)) &&
        !departments.every(department => selectedIds.has(department.departmentId));


    return (
        <div className="bg-white rounded-lg shadow">
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableCell isHeader>
                            <CheckboxItem
                                id="select-all"
                                checked={isAllSelected}
                                indeterminate={isIndeterminate}
                                onChange={(id, checked) => onSelectAll(departments.map(department => department.departmentId), checked)}
                            />
                        </TableCell>
                        <TableCell isHeader>STT</TableCell>
                        <TableCell isHeader>Department Name</TableCell>
                        <TableCell isHeader>Department Code</TableCell>

                        <TableCell isHeader>Department Parent</TableCell>
                        <TableCell isHeader>Trạng thái</TableCell>
                        <TableCell isHeader>Thao tác</TableCell>
                    </TableRow>
                </TableHeader>

                <TableBody>
                    {departments.map(department => (
                        <TableRow key={department.departmentId}>
                            <TableCell>
                                <CheckboxItem
                                    id={department.departmentId}
                                    checked={selectedIds.has(department.departmentId)}
                                    onChange={onSelectOne}
                                />
                            </TableCell>
                            <TableCell>{department.departmentId}</TableCell>
                            <TableCell>{department.departmentName}</TableCell>
                            <TableCell>{department.departmentCode}</TableCell>
                            <TableCell>{department?.departmentParentName}</TableCell>

                            <TableCell>

                                 <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                                     department.status === 1
                                         ? 'bg-green-100 text-green-800'
                                         : 'bg-red-100 text-red-800'
                                 }`}>
                        {department.status === 1 ? 'Hoạt động' : 'Không hoạt động'}
                      </span>
                            </TableCell>
                            <TableCell>
                                <div className="flex space-x-2">
                                    <Button
                                        type="edit"
                                        size="sm"
                                        onClick={() => onEdit(department)}
                                        className="text-blue-600 hover:text-blue-800"
                                    />
                                    <Button
                                        type="delete"
                                        size="sm"
                                        onClick={() => onDelete(department)}
                                        className="text-red-600 hover:text-red-800"
                                    />
                                </div>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

        </div>
    );
}

DepartmentList.propTypes = {
    departments: PropTypes.array.isRequired,
    selectedIds: PropTypes.object.isRequired,
    onSelectAll: PropTypes.func.isRequired,
    onSelectOne: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired
}




export default DepartmentList;