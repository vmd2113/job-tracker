import React, {useState, useEffect} from 'react';
import {ChevronRight, ChevronDown} from 'lucide-react';

const TreeSelectDepartment = ({
                                  value,
                                  onChange,
                                  isLoading,
                                  placeholder = "Chọn phòng ban",
                                  className = ""
                              }) => {
    // State for expanded department IDs
    const [expandedDepts, setExpandedDepts] = useState(new Set());
    // State for storing all departments
    const [departments, setDepartments] = useState([]);
    // State for tracking hover state
    const [hoveredDept, setHoveredDept] = useState(null);
    // State for dropdown visibility
    const [isOpen, setIsOpen] = useState(false);
    // State for selected department
    const [selectedDept, setSelectedDept] = useState(null);

    // Function to toggle department expansion
    const toggleDepartment = (deptId, event) => {
        event.stopPropagation();
        const newExpanded = new Set(expandedDepts);
        if (newExpanded.has(deptId)) {
            newExpanded.delete(deptId);
        } else {
            newExpanded.add(deptId);
        }
        setExpandedDepts(newExpanded);
    };

    // Function to render a single department item
    const renderDepartmentItem = (dept, level = 0) => {
        const hasChildren = dept.children && dept.children.length > 0;
        const isExpanded = expandedDepts.has(dept.departmentId);
        const isHovered = hoveredDept === dept.departmentId;
        const isSelected = value === dept.departmentId;

        return (
            <div key={dept.departmentId}>
                <div
                    className={`
            flex items-center px-4 py-2 cursor-pointer
            ${isSelected ? 'bg-blue-100' : isHovered ? 'bg-gray-100' : ''}
            ${level > 0 ? `ml-${level * 4}` : ''}
          `}
                    style={{paddingLeft: `${level * 16 + 16}px`}}
                    onMouseEnter={() => setHoveredDept(dept.departmentId)}
                    onMouseLeave={() => setHoveredDept(null)}
                    onClick={() => {
                        setSelectedDept(dept);
                        onChange(dept.departmentId);
                        setIsOpen(false);
                    }}
                >
                    {hasChildren && (
                        <span
                            className="mr-2 cursor-pointer"
                            onClick={(e) => toggleDepartment(dept.departmentId, e)}
                        >
              {isExpanded ? (
                  <ChevronDown className="w-4 h-4"/>
              ) : (
                  <ChevronRight className="w-4 h-4"/>
              )}
            </span>
                    )}
                    {!hasChildren && <span className="w-6"/>}
                    <span className="truncate">{dept.departmentName}</span>
                </div>

                {hasChildren && isExpanded && (
                    <div className="ml-4">
                        {dept.children.map(child => renderDepartmentItem(child, level + 1))}
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className={`relative ${className}`}>
            <div
                className="border rounded-md p-2 cursor-pointer flex justify-between items-center"
                onClick={() => setIsOpen(!isOpen)}
            >
        <span className="truncate">
          {selectedDept ? selectedDept.departmentName : placeholder}
        </span>
                <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`}/>
            </div>

            {isOpen && (
                <div
                    className="absolute w-full mt-1 border rounded-md bg-white shadow-lg z-50 max-h-60 overflow-y-auto">
                    {isLoading ? (
                        <div className="p-4 text-center text-gray-500">Loading...</div>
                    ) : (
                        departments.map(dept => renderDepartmentItem(dept))
                    )}
                </div>
            )}
        </div>
    );
};

export default TreeSelectDepartment;