import React from 'react';
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";
import PropTypes from "prop-types";


const CategoryList = ({categories, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {
    const isAllSelected = categories.length > 0 && categories.every(category => selectedIds.has(category.categoryId));
    const isIndeterminate = categories.length > 0 &&
        categories.some(category => selectedIds.has(category.categoryId)) &&
        !categories.every(category => !selectedIds.has(category.categoryId));


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
                                    onChange={(id, checked) => onSelectAll(categories.map(category => category.categoryId), checked)}
                                />
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Category Name</TableCell>
                            <TableCell isHeader>Category Code</TableCell>
                            <TableCell isHeader>Trạng thái</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>
                        </TableRow>
                    </TableHeader>

                    <TableBody>
                        {categories.map(category => (
                            <TableRow key={category.categoryId}>
                                <TableCell>
                                    <CheckboxItem
                                        id={category.categoryId}
                                        checked={selectedIds.has(category.categoryId)}
                                        onChange={onSelectOne}
                                    />
                                </TableCell>
                                <TableCell>{category.categoryId}</TableCell>
                                <TableCell>{category.categoryName}</TableCell>
                                <TableCell>{category.categoryCode}</TableCell>
                                <TableCell>

                                     <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                                         category.status === 1
                                             ? 'bg-green-100 text-green-800'
                                             : 'bg-red-100 text-red-800'
                                     }`}>
                    {category.status === 1 ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                                </TableCell>
                                <TableCell>
                                    <div className="flex space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(category)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(category)}
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

CategoryList.propTypes = {
    categories: PropTypes.arrayOf(PropTypes.object).isRequired,
    selectedIds: PropTypes.instanceOf(Set).isRequired,
    onSelectAll: PropTypes.func.isRequired,
    onSelectOne: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};

export default CategoryList;