import PropTypes from "prop-types";
import {Table, TableBody, TableCell, TableHeader, TableRow} from "../common/table/Table.jsx";
import CheckboxItem from "../common/checkbox/Checkbox.jsx";
import Button from "../common/button/Button.jsx";

const UserList = ({users, selectedIds, onSelectAll, onSelectOne, onEdit, onDelete}) => {
    const isAllSelected = users.length > 0 && users.every(user => selectedIds.has(user.userId));
    const isIndeterminate = users.length > 0 &&
        users.some(user => selectedIds.has(user.userId)) &&
        !users.every(user => selectedIds.has(user.userId));


    return (
        <div className="w-full">
            {/* Container cho toàn bộ bảng */}
            <div className="bg-white rounded-lg shadow">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableCell isHeader>
                                <div className="w-4">
                                    <CheckboxItem
                                        id="select-all"
                                        checked={isAllSelected}
                                        indeterminate={isIndeterminate}
                                        onChange={(id, checked) => onSelectAll(users.map(user => user.userId), checked)}
                                    />
                                </div>
                            </TableCell>
                            <TableCell isHeader>STT</TableCell>
                            <TableCell isHeader>Username</TableCell>
                            <TableCell isHeader>Họ</TableCell>
                            <TableCell isHeader>Tên</TableCell>
                            <TableCell isHeader>Email</TableCell>
                            <TableCell isHeader>Số điện thoại</TableCell>
                            <TableCell isHeader>Phòng ban</TableCell>
                            <TableCell isHeader>Trạng thái</TableCell>
                            <TableCell isHeader>
                                <span className="sr-only">Thao tác</span>
                            </TableCell>
                        </TableRow>
                    </TableHeader>

                    <TableBody>
                        {users.map(user => (
                            <TableRow key={user.userId}>
                                <TableCell>
                                    <div className="w-4">
                                        <CheckboxItem
                                            id={user.userId}
                                            checked={selectedIds.has(user.userId)}
                                            onChange={onSelectOne}
                                        />
                                    </div>
                                </TableCell>
                                <TableCell>{user.userId}</TableCell>
                                <TableCell>{user.username}</TableCell>
                                <TableCell>{user.firstName}</TableCell>
                                <TableCell>{user.lastName}</TableCell>
                                <TableCell>
                  <span className="truncate max-w-[200px] block">
                    {user.email}
                  </span>
                                </TableCell>
                                <TableCell>{user.phoneNumber}</TableCell>
                                <TableCell>{user.departmentName}</TableCell>
                                <TableCell>
                  <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                      user.status === 1
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                  }`}>
                    {user.status === 1 ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                                </TableCell>
                                <TableCell>
                                    <div className="flex items-center space-x-2">
                                        <Button
                                            type="edit"
                                            size="sm"
                                            onClick={() => onEdit(user)}
                                            className="text-blue-600 hover:text-blue-800"
                                        />
                                        <Button
                                            type="delete"
                                            size="sm"
                                            onClick={() => onDelete(user)}
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

UserList.propTypes = {
    users: PropTypes.arrayOf(PropTypes.object).isRequired,
    selectedIds: PropTypes.instanceOf(Set).isRequired,
    onSelectAll: PropTypes.func.isRequired,
    onSelectOne: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};


export default UserList;