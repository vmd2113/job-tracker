import {ChevronLeft, ChevronRight} from 'lucide-react';

const Pagination = ({
                        currentPage = 1,
                        totalItems = 0,
                        pageSize = 10,
                        onPageChange,
                        className = ''
                    }) => {
    // Calculate total pages based on total items and page size
    const totalPages = Math.ceil(totalItems / pageSize);
    const maxVisiblePages = 5;

    const getPageNumbers = () => {
        const pages = [];
        const halfVisible = Math.floor(maxVisiblePages / 2);
        let start = Math.max(1, currentPage - halfVisible);
        let end = Math.min(totalPages, start + maxVisiblePages - 1);

        if (end - start + 1 < maxVisiblePages) {
            start = Math.max(1, end - maxVisiblePages + 1);
        }

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }

        return pages;
    };

    if (totalPages <= 1) return null;

    return (
        <nav className={`flex items-center justify-center space-x-2 ${className}`}>
            <button
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
                className="p-2 rounded-xl hover:bg-purple-300 text-gray-500 disabled:opacity-50 disabled:cursor-not-allowed"
                aria-label="Previous page"
            >
                <ChevronLeft className="w-5 h-5"/>
            </button>

            {getPageNumbers()[0] > 1 && (
                <>
                    <button
                        onClick={() => onPageChange(1)}
                        className={`w-10 h-10 rounded-md hover:bg-gray-100 ${
                            currentPage === 1 ? 'bg-purple-400 text-white hover:bg-purple-600' : ''
                        }`}
                    >
                        1
                    </button>
                    {getPageNumbers()[0] > 2 && <span className="px-2">...</span>}
                </>
            )}

            {getPageNumbers().map((page) => (
                <button
                    key={page}
                    onClick={() => onPageChange(page)}
                    className={`w-10 h-10 rounded-md hover:bg-purple-100 ${
                        currentPage === page ? 'bg-purple-500 text-white hover:bg-purple-600' : ''
                    }`}
                >
                    {page}
                </button>
            ))}

            {getPageNumbers()[getPageNumbers().length - 1] < totalPages && (
                <>
                    {getPageNumbers()[getPageNumbers().length - 1] < totalPages - 1 && (
                        <span className="px-2">...</span>
                    )}
                    <button
                        onClick={() => onPageChange(totalPages)}
                        className={`w-10 h-10 rounded-md hover:bg-gray-100 ${
                            currentPage === totalPages ? 'bg-purple-500 text-white hover:bg-purple-600' : ''
                        }`}
                    >
                        {totalPages}
                    </button>
                </>
            )}

            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
                className="p-2 rounded-xl hover:bg-purple-300 text-gray-500 disabled:opacity-50 disabled:cursor-not-allowed"
                aria-label="Next page"
            >
                <ChevronRight className="w-5 h-5"/>
            </button>
        </nav>
    );
};

export default Pagination;