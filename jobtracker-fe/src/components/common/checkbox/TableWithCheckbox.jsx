import React from 'react';
import Checkbox from './Checkbox.jsx';

const TableWithCheckbox = () => {
    const [selectedRows, setSelectedRows] = React.useState(new Set());
    const data = [
        {id: 1, name: 'Item 1', description: 'Description 1'},
        {id: 2, name: 'Item 2', description: 'Description 2'},
        {id: 3, name: 'Item 3', description: 'Description 3'},
    ];

    const newSelected = new Set(selectedRows);

    const handleSelectAll = (id, checked) => {
        if (checked) {
            setSelectedRows(new Set(data.map(item => item.id)));
        } else {
            setSelectedRows(new Set());
        }
    };

    const handleSelectRow = (id, checked) => {

        if (checked) {
            newSelected.add(id);
        } else {
            newSelected.delete(id);
        }
        setSelectedRows(newSelected);
    };

    const isAllSelected = data.length > 0 && selectedRows.size === data.length;
    const isSomeSelected = selectedRows.size > 0 && selectedRows.size < data.length;

    return (
        <div>
            <table className="table-auto border-collapse border border-gray-300 w-full">
                <thead>
                <tr>
                    <th>
                        <Checkbox
                            id="selectAll"
                            checked={isAllSelected}
                            indeterminate={isSomeSelected}
                            onChange={handleSelectAll}
                            label="Select All"
                        />
                    </th>
                    <th>Name</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
                {data.map(row => (
                    <tr key={row.id} className={selectedRows.has(row.id) ? 'bg-gray-50' : ''}>
                        <td>
                            <Checkbox
                                id={row.id}
                                checked={selectedRows.has(row.id)}
                                onChange={handleSelectRow}
                            />
                        </td>
                        <td>{row.name}</td>
                        <td>{row.description}</td>

                    </tr>
                ))}
                </tbody>
            </table>
            <div className="mt-4 text-sm text-gray-500">
                {selectedRows.size} row(s) selected
            </div>
        </div>
    );
};

export default TableWithCheckbox;
