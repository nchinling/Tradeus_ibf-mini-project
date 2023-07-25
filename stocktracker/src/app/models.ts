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
    key: string
}

export interface RegisterResponse {
    account_id: string
    username: string
    timestamp: string
    status: string

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
    currency: string
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


export interface PortfolioData{

    account_id: string
    symbol: string
    stock_name: string
    exchange: string
    currency: string
    units: number
    buy_unit_price: number
    buy_total_price: number
    unit_current_price: number
    total_current_price: number
    total_return: number
    total_percentage_change: number
    datetime: Date

}

export interface AnnualisedPortfolioData{

    account_id: string
    symbol: string
    stock_name: string
    exchange: string
    currency: string
    units: number
    buy_unit_price: number
    buy_total_price: number
    unit_current_price: number
    total_current_price: number
    total_return: number
    annualised_profit: number
    datetime: Date

}

export interface WebSocketStock {

    symbol: string
    exchange: string
    currency: string
    price: number
    ask: number
    bid: number
    volume: number
    volumeChanged: boolean
    priceChanged: boolean
    askChanged: boolean
    bidChanged: boolean
    previousVolume: number
    previousPrice: number
    previousAsk: number
    previousBid: number
}



