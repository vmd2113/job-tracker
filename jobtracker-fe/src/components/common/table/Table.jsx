// Table.jsx
export const Table = ({ children }) => {
    return (
        <div className="w-full overflow-hidden rounded-lg shadow-md">
            {/* Container cho scroll trên mobile */}
            <div className="overflow-x-auto">
                {/* Min width đảm bảo bảng không bị vỡ trên mobile */}
                <table className="w-full min-w-[800px] divide-y divide-gray-200">
                    {children}
                </table>
            </div>
        </div>
    );
};

export const TableHeader = ({ children }) => {
    return (
        <thead className="bg-gray-50 sticky top-0 ">
        {children}
        </thead>
    );
};

export const TableBody = ({ children }) => {
    return (
        <tbody className="bg-white divide-y divide-gray-200">
        {children}
        </tbody>
    );
};

export const TableRow = ({ children }) => {
    return (
        <tr className="hover:bg-gray-100 transition-colors duration-200">
            {children}
        </tr>
    );
};

export const TableCell = ({ children, isHeader = false }) => {
    const baseClasses = "px-4 py-3 text-sm first:pl-6 last:pr-6";

    return isHeader ? (
        <th className={`${baseClasses} text-left font-medium text-gray-500 uppercase tracking-wider sticky top-0 bg-gray-50 `}>
            {children}
        </th>
    ) : (
        <td className={`${baseClasses} text-gray-700`}>
            {children}
        </td>
    );
};