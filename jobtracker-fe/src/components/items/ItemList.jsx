import React from 'react';
import PropTypes from "prop-types";
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";


const ItemList = ({
                      items, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete
                  }) => {
    const isAllSelected = items.length > 0 && items.every(item => selectedIds.has(item.itemId));
    const isIndeterminate = items.length > 0 &&
        items.some(item => selectedIds.has(item.itemId)) &&
        !items.every(item => selectedIds.has(item.itemId));


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
                                    onChange={(id, checked) => onSelectAll(items.map(item => item.itemId), checked)}
                                />
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Item Name</TableCell>
                            <TableCell isHeader>Item Code</TableCell>
                            <TableCell isHeader>Category</TableCell>
                            <TableCell isHeader>Trạng thái</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>
                        </TableRow>
                    </TableHeader>

                    <TableBody>
                        {items.map(item => (
                            <TableRow key={item.itemId}>
                                <TableCell>
                                    <CheckboxItem
                                        id={item.itemId}
                                        checked={selectedIds.has(item.itemId)}
                                        onChange={onSelectOne}
                                    />
                                </TableCell>
                                <TableCell>{item.itemId}</TableCell>
                                <TableCell>{item.itemName}</TableCell>
                                <TableCell>{item.itemCode}</TableCell>
                                <TableCell>{item.categoryName}</TableCell>

                                <TableCell>

                                     <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                                         item.status === 1
                                             ? 'bg-green-100 text-green-800'
                                             : 'bg-red-100 text-red-800'
                                     }`}>
                    {item.status === 1 ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                                </TableCell>
                                <TableCell>
                                    <div className="flex space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(item)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(item)}
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

ItemList.propTypes = {
    items: PropTypes.arrayOf(PropTypes.object).isRequired,
    selectedIds: PropTypes.instanceOf(Set).isRequired,
    onSelectAll: PropTypes.func.isRequired,
    onSelectOne: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};


export default ItemList;
