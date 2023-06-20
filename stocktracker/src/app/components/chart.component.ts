import { Component, ElementRef, Input, ViewChild, inject } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { StockService } from '../stock.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChartService } from '../chart.service';
import { Observable, map, tap } from 'rxjs';
import { ChartData } from '../models';
import { formatDate } from '@angular/common';
Chart.register(...registerables);

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent {

  fb = inject(FormBuilder)
  stockSvc = inject(StockService)
  chartSvc = inject(ChartService)
  
  @Input()
  symbol!:string

  @Input()
  initialChartSymbol!:string

  chart!:any
  loadInterval: string = '1min'

  chartForm!: FormGroup

  // chart$!: Promise<Chart>
  chart$!: Observable<ChartData>
  

  ngOnInit(): void {
    this.symbol = this.stockSvc.symbol
    console.info('the symbol in chart is: ' + this.symbol)
    this.chartForm = this.createForm()
    // this.createChart();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      interval: this.fb.control<string>(this.loadInterval, [ Validators.required ]),
      dataPoints: this.fb.control<number>(30, [ Validators.required ])
    })
  }


  processChart() {

    console.info('I am processing chart')

    const interval = this.chartForm.get('interval')?.value ?? this.loadInterval;
    const dataPoints = this.chartForm.get('dataPoints')?.value ?? 30;
    this.symbol = this.symbol !== '' ? this.symbol : this.initialChartSymbol;
    // this.symbol = this.symbol ?? this.initialChartSymbol;
    // this.symbol = this.initialChartSymbol;



    console.info('interval in processingChart is: ' + interval)
    console.info('dataPoints in processingChart is: ' + dataPoints)
    console.info('symbol in processingChart is: ' + this.symbol)
  

    if (this.symbol && interval && dataPoints) {
      console.info('>> symbol: ', this.symbol);
      console.info('>> interval: ', interval);
      console.info('>> interval: ', dataPoints);
      this.chart$ = this.chartSvc.getTimeSeries(this.symbol, interval, dataPoints); 

      
      this.chart$.subscribe(chartData => {
   
        // const labels = chartData.datetime.map(date => {
        //   const formattedDate = date instanceof Date ? date.toISOString().slice(0, 10) : '';
        //   return formattedDate;
        // });
        // const labels = chartData.datetime.map(date => {
        //   const formattedDate = date instanceof Date ? formatDate(date, 'MMM d, yyyy', 'en-US') : '';
        //   return formattedDate;
        // });
        const labels = chartData.datetime
        console.log('the labels are: '+ labels)
        const datasets = [
          {
            label: 'Open',
            data: chartData.open,
            backgroundColor: 'blue'
          },
          {
            label: 'High',
            data: chartData.high,
            backgroundColor: 'limegreen'
          },
          {
            label: 'Low',
            data: chartData.low,
            backgroundColor: 'red'
          },
          {
            label: 'Close',
            data: chartData.close,
            backgroundColor: 'yellow'
          }
        ];

        if (this.chart) {
          // Destroy the previous chart if it exists
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
            maintainAspectRatio: false
          }
        });
      });
    }
  



    }

}


  // createChart(){
  
  //   this.chart = new Chart("MyChart", {
  //     type: 'line', //this denotes tha type of chart

  //     data: {// values on X-Axis
  //       labels: ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13',
	// 							 '2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ], 
	//        datasets: [
  //         {
  //           label: "Sales",
  //           data: ['467','576', '572', '79', '92',
	// 							 '574', '573', '576'],
  //           backgroundColor: 'blue'
  //         },
  //         {
  //           label: "Profit",
  //           data: ['542', '542', '536', '327', '17',
	// 								 '0.00', '538', '541'],
  //           backgroundColor: 'limegreen'
  //         }  
  //       ]
  //     },
  //     options: {
       
  //         responsive: true,
  //         maintainAspectRatio: false,
       
      
  //     }
      
  //   });
  // }


  // createChart() {
   
  //     this.chart$.subscribe(chartData => {
  //       const labels = chartData.datetime.map(date => date.toISOString().slice(0, 10));
  //       console.log('the labels are:' + labels)
  //       console.log('the open array are: '+ chartData.open)
  //       const datasets = [
  //         {
  //           label: 'Open',
  //           data: chartData.open,
  //           backgroundColor: 'blue'
  //         },
  //         {
  //           label: 'High',
  //           data: chartData.high,
  //           backgroundColor: 'limegreen'
  //         },
  //         {
  //           label: 'Low',
  //           data: chartData.low,
  //           backgroundColor: 'red'
  //         },
  //         {
  //           label: 'Close',
  //           data: chartData.close,
  //           backgroundColor: 'yellow'
  //         }
  //       ];
  
  //       this.chart = new Chart('MyChart', {
  //         type: 'line',
  //         data: {
  //           labels: labels,
  //           datasets: datasets
  //         },
  //         options: {
  //           responsive: true,
  //           maintainAspectRatio: false
  //         }
  //       });
  //     });
  //   }
  


  

