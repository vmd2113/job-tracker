```jsx
// App.jsx
import React from "react";
import {ToastProvider, useToast} from "./components/common/toast/ToastContainer";
import {Table, TableHeader, TableBody, TableRow, TableCell} from "./components/common/table/Table";

function TestButtons() {
    const {showToast} = useToast();

    const handleSuccess = () => {
        showToast('Thêm mới thành công!', 'success', 3000);
    };

    const handleError = () => {
        showToast('Có lỗi xảy ra!', 'error', 5000);
    };

    return (
        <div>
            <button onClick={handleSuccess}>Test Success Toast</button>
            <button onClick={handleError}>Test Error Toast</button>
        </div>
    );
}

function App() {


    const users = [
        {name: "Lindsay Walton", title: "Front-end Developer", email: "lindsay.walton@example.com", role: "Member"},
        {name: "Courtney Henry", title: "Designer", email: "courtney.henry@example.com", role: "Admin"},
        {name: "Tom Cook", title: "Director of Product", email: "tom.cook@example.com", role: "Member"},
        {name: "Whitney Francis", title: "Copywriter", email: "whitney.francis@example.com", role: "Admin"},
        {name: "Leonard Krasner", title: "Senior Designer", email: "leonard.krasner@example.com", role: "Owner"},
        {name: "Floyd Miles", title: "Principal Designer", email: "floyd.miles@example.com", role: "Member"},
    ];
    return (
        <ToastProvider>

            <TestButtons/>
            <div>
                <div className="p-6">
                    <h1 className="text-xl font-semibold text-gray-800">Users</h1>
                    <p className="text-sm text-gray-600 mb-4">
                        A list of all the users in your account including their name, title, email and role.
                    </p>
                    <button
                        className="bg-blue-600 text-white px-4 py-2 rounded-lg mb-4 hover:bg-blue-700 transition">
                        Add user
                    </button>
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableCell isHeader>Name</TableCell>
                                <TableCell isHeader>Title</TableCell>
                                <TableCell isHeader>Email</TableCell>
                                <TableCell isHeader>Role</TableCell>
                                <TableCell isHeader></TableCell>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {users.map((user, index) => (
                                <TableRow key={index}>
                                    <TableCell>{user.name}</TableCell>
                                    <TableCell>{user.title}</TableCell>
                                    <TableCell>{user.email}</TableCell>
                                    <TableCell>{user.role}</TableCell>
                                    <TableCell>
                                        <a href="#" className="text-blue-600 hover:text-blue-900">Edit</a>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </div>
        </ToastProvider>
    );
    
}

export default App;
```