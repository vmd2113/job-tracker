import axios from "axios";


const getTimestamp = () => new Date().toISOString();

// Khởi tạo axiosInstance
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // Port của API Gateway
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    withCredentials: true
});

// Interceptor cho Request
axiosInstance.interceptors.request.use(
    (config) => {

        const accessToken = localStorage.getItem('accessToken');
        console.log("ACCESS TOKEN", accessToken);
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }

        // Log thông tin request
        console.log(`[${getTimestamp()}] [REQUEST] ${config.method.toUpperCase()} ${config.url}`);
        console.log(`[${getTimestamp()}] [REQUEST] Headers:`, config.headers);
        console.log(`[${getTimestamp()}] [REQUEST] Body:`, config.data);

        return config;
    },
    (error) => {
        console.error(`[${getTimestamp()}] [REQUEST ERROR] ${error.message}`);
        return Promise.reject(error);
    }
);

// Interceptor cho Response
axiosInstance.interceptors.response.use(
    (response) => {
        // Log thông tin phản hồi thành công
        console.log(`[${getTimestamp()}] [RESPONSE] ${response.status} ${response.config.url}`);
        console.log(`[${getTimestamp()}] [RESPONSE] Data:`, response.data);

        return response;
    },
    async (error) => {
        // Log thông tin lỗi
        console.error(`[${getTimestamp()}] [RESPONSE ERROR] ${error.message}`);

        if (error.response?.status === 401) {
            console.error(`[${getTimestamp()}] [RESPONSE ERROR] Unauthorized - Token expired or invalid`);
            const refreshToken = localStorage.getItem('refreshToken');

            if (refreshToken) {
                try {
                    console.log(`[${getTimestamp()}] [RESPONSE ERROR] Attempting to refresh token...`);
                    const newTokenResponse = await axios.post('/auth/refresh', {refreshToken});
                    localStorage.setItem('token', newTokenResponse.data.accessToken);

                    // Thêm token mới vào request cũ và gửi lại
                    error.config.headers.Authorization = `Bearer ${newTokenResponse.data.accessToken}`;
                    console.log(`[${getTimestamp()}] [RESPONSE ERROR] Retrying the original request...`);
                    return axiosInstance.request(error.config);
                } catch (refreshError) {
                    console.error(`[${getTimestamp()}] [RESPONSE ERROR] Failed to refresh token`, refreshError);
                    window.location.href = '/auth'; // Nếu refresh token không thành công, chuyển hướng đến auth
                }
            } else {
                console.error(`[${getTimestamp()}] [RESPONSE ERROR] No refresh token found`);
                window.location.href = '/auth'; // Chuyển hướng đến auth nếu không có refresh token
            }
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
