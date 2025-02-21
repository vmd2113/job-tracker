import React from 'react';
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";
import PropTypes from "prop-types";


const WorkConfigList = ({workConfigs, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {
    const isAllSelected = workConfigs.length > 0 && workConfigs.every(workConfig => selectedIds.has(workConfig.workConfigId));
    const isIndeterminate = workConfigs.length > 0 &&
        workConfigs.some(workConfig => selectedIds.has(workConfig.workConfigId)) &&
        !workConfigs.every(workConfig => !selectedIds.has(workConfig.workConfigId));


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
                                    onChange={(id, checked) => onSelectAll(workConfigs.map(workConfig => workConfig.workConfigId), checked)}
                                />
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Tên loại công việc</TableCell>
                            <TableCell isHeader>Mức độ ưu tiên</TableCell>
                            <TableCell isHeader>Trạng thái cũ</TableCell>
                            <TableCell isHeader>Trạng thái mới</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>
                        </TableRow>
                    </TableHeader>

                    <TableBody>
                        {workConfigs.map(workConfig => (
                            <TableRow key={workConfig.workConfigId}>
                                <TableCell>
                                    <CheckboxItem
                                        id={workConfig.workConfigId}
                                        checked={selectedIds.has(workConfig.workConfigId)}
                                        onChange={onSelectOne}
                                    />
                                </TableCell>
                                <TableCell>{workConfig.workConfigId}</TableCell>
                                <TableCell>{workConfig.workTypeName}</TableCell>
                                <TableCell>{workConfig.priorityName}</TableCell>
                                <TableCell>{workConfig.oldStatusName}</TableCell>
                                <TableCell>{workConfig.newStatusName}</TableCell>
                                <TableCell>
                                    <div className="flex space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(workConfig)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(workConfig)}
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

export default WorkConfigList;