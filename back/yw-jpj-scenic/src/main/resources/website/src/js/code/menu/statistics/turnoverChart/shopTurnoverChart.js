import { windowCenter, setConfig } from 'common/commonClass'
import ChartPanel from 'component/chartPanel'

export default function shopTurnoverChart (opt) {
    var chartOptions = {
        title: {
            text: '商铺交易量'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['交易金额','数量']
        },
        toolbox: {
            show: true,
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                data: [],
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value}元'
                }
            },{
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '交易额',
                type: 'bar',
                data: [],
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },

                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }, {
                name:'数量',
                type:'line',
                yAxisIndex: 1,
                data:[]
            }
        ]
    }
    return new ChartPanel(setConfig({
        opt: opt,
        fnName: 'shopTurnoverChart',
        classGroup: 'ChartPanel',
        config: {
            container: windowCenter,
            url: '/statistical/totalTradeStore',
            type: 'get',
            chartOptions: chartOptions,
            chartHeight: 500,
            ajaxSuccess: function (opt) {
                if (opt.response && opt.response.data && opt.response.data.length) {
                    let xList = []
                    let yBarList = []
                    let lineList = []
                    opt.response.data.map((item, index, list) => {
                        xList.push(item.storeName);
                        yBarList.push(item.money);
                        lineList.push(item.count);
                    })
                    opt.panel.chartOptions.xAxis[0].data = xList
                    opt.panel.chartOptions.series[0].data = yBarList
                    opt.panel.chartOptions.series[1].data = lineList
                }
            },
            boxExtraClass: ['col-sm-12'],
            title: {
                innerHTML: '商铺交易量'
            },
            search: {
                fields: [
                    {
                        name: 'startDate',
                        text: '请选择起始日期',
                        type: 'startTime'
                    },
                    {
                        name: 'endDate',
                        text: '结束日期',
                        type: 'endTime'
                    }
                ]
            }
            // render:(opt)=>{}
        }
    }))
}
