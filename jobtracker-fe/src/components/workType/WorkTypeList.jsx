import React from 'react';
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";
import PropTypes from "prop-types";


const WorkTypeList = ({workTypes, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {

    const isAllSelected = workTypes.length > 0 && workTypes.every(workType => selectedIds.has(workType.workTypeId));
    const isIndeterminate = workTypes.length > 0 &&
        workTypes.some(workType => selectedIds.has(workType.workTypeId)) &&
        !workTypes.every(workType => selectedIds.has(workType.workTypeId));


    return (

        <div className="w-full">
            <div className="bg-white rounded-lg shadow">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableCell isHeader>
                                <CheckboxItem
                                    id="select-all"
                                    checked={isAllSelected}
                                    indeterminate={isIndeterminate}
                                    onChange={(id, checked)=> onSelectAll(workTypes.map(workType=>workType.workTypeId), checked)}
                                />
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Work Type Name</TableCell>
                            <TableCell isHeader>Work Type Code</TableCell>
                            <TableCell isHeader>Process Time</TableCell>
                            <TableCell isHeader>Trạng thái</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>
                        </TableRow>

                    </TableHeader>

                    <TableBody>
                        {workTypes.map(workType => (
                            <TableRow key={workType.workTypeId}>
                                <TableCell>
                                    <CheckboxItem
                                        id={workType.workTypeId}
                                        checked={selectedIds.has(workType.workTypeId)}
                                        onChange={onSelectOne}
                                    />
                                </TableCell>
                                <TableCell>{workType.workTypeId}</TableCell>
                                <TableCell>{workType.workTypeName}</TableCell>
                                <TableCell>{workType.workTypeCode}</TableCell>
                                <TableCell>{workType.processTime}</TableCell>
                                <TableCell>
                                    <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                                        workType.status === 1
                                            ? 'bg-green-100 text-green-800'
                                            : 'bg-red-100 text-red-800'
                                    }`}>
                    {workType.status === 1 ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                                </TableCell>
                                <TableCell>
                                    <div className="flex space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(workType)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(workType)}
                                            className="text-red-600 hover:text-red-800"
                                        />
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </div>
    );
};

WorkTypeList.propTypes = {
    workTypes: PropTypes.arrayOf(PropTypes.object).isRequired,
    selectedIds: PropTypes.instanceOf(Set).isRequired,
    onSelectAll: PropTypes.func.isRequired,
    onSelectOne: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};

export default WorkTypeList;