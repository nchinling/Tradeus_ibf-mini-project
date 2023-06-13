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
    account_id: string
    username: string
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

export interface Stock{
    symbol: string
    name: string
    exchange: string
    currency: string

}

