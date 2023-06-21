import { NgbHighlight } from "@ng-bootstrap/ng-bootstrap"

export interface UserData {
    account_id: string
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

    open: number
    high: number
    low: number
    close: number
    volume: number
    previous_close: number
    change: number
    percent_change: number
    //use datePipe for datetime
    // <p>DateTime: {{ stockData.datetime | date:'yyyy-MM-dd HH:mm:ss' }}</p>
    datetime: Date
}

export interface MarketIndex{
    symbol: string
    name: string
    datetime: Date
    close: number
    change: number
    percentage_change: number
}

export interface Market {
    symbol: string;
    interval: string;
}

export interface StockInfo {

    symbol: string
    name: string
    currency: string
    exchange: string 
    country: string
    type: string

}

export interface StockProfile {

    symbol: string
    name: string
    sector: string
    industry: string 
    ceo: string
    employees: number
    website: string
    description: string
    logoUrl: string

}


export interface ChartData{

    symbol: string
    interval: string
    datetime: Date[]
    open: number[]
    high: number[]
    low: number[]
    close: number[]

}

export interface TradeData{

    exchange: string
    stockName: string
    symbol: string
    units: number
    price: number
    fee: number
    date: Date

}

export interface TradeResponse{

    account_id: string
    username: string
    exchange: string
    stockName: string
    symbol: string
    units: number
    price: number
    fee: number
    date: Date

}



