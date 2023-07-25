import { Component, Injectable, Input, SimpleChanges, inject } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { StockService } from '../stock.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChartService } from '../chart.service';
import { Observable } from 'rxjs';
import { ChartData } from '../models';
import { ResearchComponent } from './research.component';
Chart.register(...registerables);

@Injectable()
@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css'],
})
export class ChartComponent {

  fb = inject(FormBuilder)
  stockSvc = inject(StockService)
  chartSvc = inject(ChartService)
  researchComp = inject(ResearchComponent)
  

  symbol!:string

  stock_name!:string


  chart!:any
  loadInterval: string = '1min'

  chartForm!: FormGroup


  chart$!: Observable<ChartData>
  

  ngOnInit(): void {
    this.symbol = this.stockSvc.symbol
    console.info('the symbol in chart is: ' + this.symbol)
    this.chartForm = this.createForm()
    this.researchComp.updatedChartData.subscribe((data) => {
      this.symbol = data.symbol;
      this.stock_name = data.stock_name;
    this.processChart()
    })
    
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (
      changes['symbol'] &&
      !changes['symbol'].firstChange &&
      changes['symbol'].currentValue !== changes['symbol'].previousValue
    ) {
      this.processChart();
    }
  }


  private createForm(): FormGroup {
    return this.fb.group({
      interval: this.fb.control<string>(this.loadInterval, [ Validators.required ]),
      dataPoints: this.fb.control<number>(30, [ Validators.required,  Validators.min(1), Validators.max(5000) ])
    })
  }

  invalidField(ctrlName:string): boolean{
    return !!(this.chartForm.get(ctrlName)?.invalid && this.chartForm.get(ctrlName)?.dirty)
  }


  processChart() {

    console.info('I am processing chart')
    console.info('this.symbol in processChart is' + this.symbol)


    const interval = this.chartForm.get('interval')?.value ?? this.loadInterval;
    const dataPoints = this.chartForm.get('dataPoints')?.value ?? 30;
    // this.symbol = this.symbol !== '' ? this.symbol : this.initialChartSymbol;


    console.info('interval in processingChart is: ' + interval)
    console.info('dataPoints in processingChart is: ' + dataPoints)
    console.info('symbol in processingChart is: ' + this.symbol)
  

    if (this.symbol && interval && dataPoints) {
      console.info('>> symbol: ', this.symbol);
      console.info('>> interval: ', interval);
      console.info('>> interval: ', dataPoints);
      this.chart$ = this.chartSvc.getTimeSeries(this.symbol, interval, dataPoints); 

      
      this.chart$.subscribe(chartData => {
   
        const labels = chartData.datetime.reverse()
        console.log('the labels are: '+ labels)
        const datasets = [
          {
            label: 'Open',
            data: chartData.open,
            backgroundColor: 'blue',
            borderColor:'blue'
          },
          {
            label: 'High',
            data: chartData.high,
            backgroundColor: 'limegreen',
            borderColor:'limegreen'
          },
          {
            label: 'Low',
            data: chartData.low,
            backgroundColor: 'red',
            borderColor:'red'
          },
          {
            label: 'Close',
            data: chartData.close,
            backgroundColor: 'yellow',
            borderColor:'yellow'
          }
        ];

        if (this.chart) {
          this.chart.destroy();
        }
  
        this.chart = new Chart('MyChart', {
          type: 'line',
          data: {
            labels: labels,
            datasets: datasets
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              x:{
                display: false
              }
            },
            plugins:{
              title: {
                display: true,
                text: this.stock_name,
                font:{
                  size: 20
                }
              }
            }
       
          }

        });
      });
    }
  

    }

}


