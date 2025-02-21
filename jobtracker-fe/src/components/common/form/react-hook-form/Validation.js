// validation.js

export const notAdmin = (value) => {
    return value !== "admin" || "Không được sử dụng username là admin";
};

export const notBlackListed = (value) => {
    const blackList = ["test", "fake", "dummy"];
    return !blackList.includes(value) || "Username này không được phép sử dụng";
};

export const checkUserExists = async (value) => {
    const response = await fetch(`/api/check-username/${value}`);
    return (await response.json()).isAvailable || "Username đã tồn tại";
};
