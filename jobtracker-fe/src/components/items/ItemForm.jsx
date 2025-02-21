import React, {useEffect} from 'react';
import {Controller, useForm} from "react-hook-form";
import useCategoryManagement from "../../hooks/category/useCategoryManagement.jsx";
import FormInput from "../common/input/FormInput.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import useItemManagement from "../../hooks/items/useItemManagement.jsx";


const ItemForm = React.forwardRef(({
                                       initialData,
                                       onSubmit,
                                       type
                                   }, ref) => {

    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger
    } = useForm({
        defaultValues: initialData || {
            itemName: initialData?.itemName || '',
            itemCode: initialData?.itemCode || '',
            itemValue: initialData?.itemValue || 0,
            itemParentId: initialData?.itemParentId || null,
            categoryId: initialData?.categoryId || null,
            status: type === 'add' ? 1 : (initialData?.status ?? 1),
        },
        mode: 'onTouched'
    });

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    const {fetchAllCategories} = useCategoryManagement();
    const {fetchItems} = useItemManagement();
    const [items, setItems] = React.useState([]);
    const [categories, setCategories] = React.useState([]);
    const [loading, setLoading] = React.useState(false);
    const [error, setError] = React.useState(null);


    const listCategories = React.useMemo(() => {
        return categories.map(category => ({
                value: category.categoryId,
                label: category.categoryCode
            }
        ));
    }, [categories]);


    const listItems = React.useMemo(() => {
        return items.map(item => ({
            value: item.itemId,
            label: `${item.itemName} - ${item.itemCode}`
        }));
    }, [items]);


    useEffect(() => {
        const loadCategories = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await fetchAllCategories();
                console.log("ITEM FORM -> FETCH ALL CATEGORIES", response);
                if (response && Array.isArray(response)) {
                    setCategories(response);
                }

            } catch (err) {
                setError(err.message || 'Failed to load categories');
                console.error('Error loading categories:', err);
            } finally {
                setLoading(false);
            }
        };


        loadCategories();
    }, [fetchAllCategories]);

    useEffect(() => {
        const loadItems = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await fetchItems();
                if (response?.data && Array.isArray(response.data)) {
                    setItems(response.data);
                }
            } catch (err) {
                setError(err.message || 'Failed to load items');
                console.error('Error loading items:', err);
            } finally {
                setLoading(false);
            }
        };

        loadItems();
    }, [fetchItems]);

    console.log("ITEM FORM -> ITEM FORM");
    console.log("ITEM FORM -> CATEGORY LIST", categories);
    console.log("ITEM FORM -> LIST CATEGORIES", listCategories);
    React.useImperativeHandle(ref, () => ({
        submit: () => {
            trigger().then(isValid => {
                if (isValid) {
                    handleSubmit(onSubmit)();
                }
            });
        }
    }))

    return (
        <div className="relative w-full">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6" autoComplete="off">
                {error && (
                    <div className="p-4 mb-4 text-sm text-red-600 bg-red-50 rounded-lg">
                        {error}
                    </div>
                )}
                {/* Item Name Field */}
                <div className="space-y-4">

                    <FormInput
                        control={control}
                        name="itemName"
                        label="Item Name"
                        type="text"
                        placeholder="Nhập tên hạng mục"
                        rules={{
                            required: 'Item Name là bắt buộc',
                            minLength: {
                                value: 6,
                                message: 'Item Name phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 500,
                                message: 'Item Name không được vượt quá 500 ký tự'
                            }
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full mt-1"
                    />


                    {/* Item Code Field */}

                    <FormInput
                        control={control}
                        name="itemCode"
                        label="Item Code"
                        type="text"
                        placeholder="Nhập code hạng mục"
                        rules={{
                            required: 'Item Code là bắt buộc',
                            minLength: {
                                value: 3,
                                message: 'Item Code phải có ít nhất 3 ký tự'
                            },
                            maxLength: {
                                value: 100,
                                message: 'Item Code không được vượt quá 100 ký tự'
                            }
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full mt-1"
                    />

                    <FormInput
                        control={control}
                        name="itemValue"
                        label="Item Value"
                        type="number"
                        placeholder="Nhập giá trị hạng mục"
                        rules={{
                            required: 'Item Value là bắt buộc'
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full mt-1"
                    />


                    <label className="block text-sm font-medium text-gray-700">
                        Category
                    </label>
                    <div className="relative">
                        <Controller
                            name="categoryId"  // Changed from status to categoryId
                            control={control}
                            rules={{
                                required: "Category là bắt buộc"
                            }}
                            render={({field}) => (
                                <InputSelection
                                    options={listCategories}
                                    value={field.value}
                                    onChange={field.onChange}
                                    placeholder="Chọn mã danh mục"  // Updated placeholder text
                                    className="w-full"
                                    isSearchable={true}  // Enable search feature
                                    isClearable={true}   // Allow clearing the selection
                                    menuPortalTarget={document.body}
                                    styles={{
                                        menuPortal: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        }),
                                        menu: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        })
                                    }}
                                />
                            )}
                        />
                        {errors.categoryId && (  // Changed from status to categoryId
                            <p className="mt-1 text-sm text-red-600">
                                {errors.categoryId.message}
                            </p>
                        )}
                    </div>

                    <label className="block text-sm font-medium text-gray-700">
                        Chọn item cha
                    </label>
                    <div className="relative">
                        <Controller
                            name="status"
                            control={control}
                            render={({field}) => (
                                <InputSelection
                                    options={listItems}
                                    value={field.value}
                                    onChange={field.onChange}
                                    placeholder="Chọn item cha"
                                    className="w-full"
                                    menuPortalTarget={document.body}
                                    styles={{
                                        menuPortal: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        }),
                                        menu: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        })
                                    }}
                                />
                            )}
                        />
                        {errors.status && (
                            <p className="mt-1 text-sm text-red-600">
                                {errors.status.message}
                            </p>
                        )}
                    </div>

                    <label className="block text-sm font-medium text-gray-700">
                        Trạng thái
                    </label>
                    <div className="relative">
                        <Controller
                            name="status"
                            control={control}
                            rules={{
                                required: "Trạng thái là bắt buộc"
                            }}
                            render={({field}) => (
                                <InputSelection
                                    options={STATUS_OPTIONS}
                                    value={field.value}
                                    onChange={field.onChange}
                                    placeholder="Chọn trạng thái"
                                    className="w-full"
                                    disabled={type === 'add'}
                                    menuPortalTarget={document.body}
                                    styles={{
                                        menuPortal: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        }),
                                        menu: (base) => ({
                                            ...base,
                                            zIndex: 9999
                                        })
                                    }}
                                />
                            )}
                        />
                        {errors.status && (
                            <p className="mt-1 text-sm text-red-600">
                                {errors.status.message}
                            </p>
                        )}
                    </div>
                </div>
            </form>
        </div>


    );
});
export default ItemForm;



