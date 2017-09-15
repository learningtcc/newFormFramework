import { windowCenter, setConfig } from 'common/commonClass'
import ChartPanel from 'component/chartPanel'

export default function goodsSoldChart (opt) {
    var chartOptions = {
        title: {
            text: '商品销售排行'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['销售量']
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
                type: 'value'
            }
        ],
        yAxis: [
            {
              type: 'category',
              data: []
            }
        ],
        series: [
            {
                name: '销售数量',
                type: 'bar',
                data: [],
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    }
    return new ChartPanel(setConfig({
        opt: opt,
        fnName: 'goodsSoldChart',
        classGroup: 'ChartPanel',
        config: {
            container: windowCenter,
            url: '/statistical/countCommoditySales',
            type: 'get',
            chartOptions: chartOptions,
            chartHeight: 500,
            ajaxSuccess: function (opt) {
                if (opt.response && opt.response.data && opt.response.data.length) {
                    let xList = []
                    let yBarList = []
                    opt.response.data.map((item, index, list) => {
                        xList.push(item.name)
                        yBarList.push(item.count)
                    })
                    opt.panel.chartOptions.yAxis[0].data = xList
                    opt.panel.chartOptions.series[0].data = yBarList
                }
            },
            boxExtraClass: ['col-sm-12'],
            title: {
                innerHTML: '商品销售排行'
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
