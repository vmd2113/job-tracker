import React from 'react';
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";
import PropTypes from "prop-types";


const WorkList = ({works, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {
    const isAllSelected = works.length > 0 && works.every(work => selectedIds.has(work.worksId));
    const isIndeterminate = works.length > 0 &&
        works.some(work => selectedIds.has(work.worksId)) &&
        !works.every(work => selectedIds.has(work.worksId));

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
                                    onChange={(id, checked) => onSelectAll(works.map(work => work.worksId), checked)}
                                />
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Mã công việc</TableCell>
                            <TableCell isHeader>Nội dung công việc</TableCell>
                            <TableCell isHeader>Tên loại công việc</TableCell>
                            <TableCell isHeader>Mức độ ưu tiên</TableCell>
                            <TableCell isHeader>Thời gian bắt đầu</TableCell>
                            <TableCell isHeader>Thời gian kết thúc</TableCell>
                            <TableCell isHeader>Trạng thái</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>

                        </TableRow>
                    </TableHeader>

                    <TableBody>

                        {works.map(work=> (
                            <TableRow key={work.worksId}>
                                <TableCell>
                                    <CheckboxItem
                                        id={work.worksId}
                                        checked={selectedIds.has(work.worksId)}
                                        onChange={onSelectOne}
                                    />
                                </TableCell>
                                <TableCell>{work.worksId}</TableCell>
                                <TableCell>{work.workCode}</TableCell>
                                <TableCell>{work.workContent}</TableCell>
                                <TableCell>{work.workTypeName}</TableCell>
                                <TableCell>{work.priorityName}</TableCell>
                                <TableCell>{work.startTime}</TableCell>
                                <TableCell>{work.endTime}</TableCell>
                                <TableCell>{work.statusName}</TableCell>
                                <TableCell>
                                    <div className="flex space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(work)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(work)}
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

export default WorkList;