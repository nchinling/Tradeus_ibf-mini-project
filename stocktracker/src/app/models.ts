export interface RegisterData {

    name: string
    username: string
    password: string
    mobile_no: string
    nationality: string
    address: string
    date_of_birth: Date

}

export interface LoginResponse {
    username: string
    password: string
    timestamp: string
}

export interface RegisterResponse {
    account_id: string
    username: string
    timestamp: string
    status: string
    // error?: string; 

}

export interface ErrorResponse {

    error: string; 

}