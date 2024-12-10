








/**
* @method : monthYearChange
* @desc : 월별 차트 년도별 차트 전환
* @author : LeeChanSin
*/
function monthYearChange(tagName) {
  var today = new Date();
  var categories = [];
  if (tagName === 'chartMonth') {
      var currentMonth = today.getMonth(); // 현재 월 (0부터 시작)
      var currentYear = today.getFullYear(); // 현재 년도

      // 해당 월의 첫 번째 날짜를 가져옵니다.
      var firstDayOfMonth = new Date(currentYear, currentMonth, 1);

      // 해당 월의 마지막 날짜를 가져옵니다.
      var lastDayOfMonth = new Date(currentYear, currentMonth + 1, 0);
      var totalDays = lastDayOfMonth.getDate(); // 해당 월의 총 일수

      for (var i = 1; i <= totalDays; i++) {
          categories.push(i);

      }
      return categories;
  } else if (tagName === 'chartYear') {
      for (var i = 1; i <= 12; i++) {
          categories.push(i);
      }
      return categories;
  }

}

let categoriesMonth = monthYearChange('chartMonth');
// 이번달 차트
var optionMonth = {
    series: [{

        data: monthData // 날짜별 결제 내역갯수
    }],
    chart: { // 차트 전체 크기
        height: '80%',
        width: '100%',
        type: 'bar',
        toolbar: {
            show: false, // 다운로드 버튼 숨기기
        },

    },
    plotOptions: {
        bar: {
            columnWidth: '15%', // bar 너비 조절

        }
    },
    dataLabels: {
        enabled: false, // 컬럼바 위 텍스트 표시x

        style: {
            fontSize: '12px', // bar의 폰트 크기
            colors: ["#304758"]
        }
    },
    tooltip: {
        enabled: true, // 툴팁 활성화
        custom: function({
            series,
            seriesIndex,
            dataPointIndex,
            w
        }) {
            // 툴팁 커스텀 내용을 생성합니다.
            return '<div class="custom-tooltip">' +
                '<span>' + series[seriesIndex][dataPointIndex] + '</span>' +
                '</div>';
        }
    },
    xaxis: {
        categories: categoriesMonth,
        position: 'bottom',
        labels: {
            show: true,
        },
        axisBorder: {
            show: false,
        },
        axisTicks: {
            show: false,
        },
    },
    yaxis: {
        show: true,
        opposite: true, // y축을 오른쪽에 배치합니다.
        forceNiceScale: true,
        labels: {
            show: true,
            formatter: function(val) {
                return val;
            }

        },




    },

  };


  var chartMonth = new ApexCharts(document.querySelector("#chartMonth"), optionMonth);
  chartMonth.render();



    let categoriesYear = monthYearChange('chartYear');
    // 년도 기준 월별 차트
    var optionYear = {
        series: [{

            data: yearData // 날짜별 결제 내역갯수
        }],
        chart: { // 차트 전체 크기
            height: '80%',
            width: '100%',
            type: 'bar',
            toolbar: {
                show: false, // 다운로드 버튼 숨기기
            },

        },
        plotOptions: {
            bar: {
                columnWidth: '15%', // bar 너비 조절

            }
        },
        dataLabels: {
            enabled: false, // 컬럼바 위 텍스트 표시x

            style: {
                fontSize: '12px', // bar의 폰트 크기
                colors: ["#304758"]
            }
        },
        tooltip: {
            enabled: true, // 툴팁 활성화
            custom: function({
                series,
                seriesIndex,
                dataPointIndex,
                w
            }) {
                // 툴팁 커스텀 내용을 생성합니다.
                return '<div class="custom-tooltip">' +
                    '<span>' + series[seriesIndex][dataPointIndex] + '</span>' +
                    '</div>';
            }
        },
        xaxis: {
            categories: categoriesYear,
            position: 'bottom',
            labels: {
                show: true,
            },
            axisBorder: {
                show: false,
            },
            axisTicks: {
                show: false,
            },
        },
        yaxis: {
            show: true,
            opposite: true, // y축을 오른쪽에 배치합니다.
            forceNiceScale: true,
            labels: {
                show: true,
                formatter: function(val) {
                    return val;
                }

            },




        },

      };

    var chartYear = new ApexCharts(document.querySelector("#chartYear"), optionYear);
    chartYear.render();



    /**
    * @method : openTab
    * @desc : 월별 차트 년도별 차트 전환
    * @author : LeeChanSin
    */
    function openTab(tabName) {
        var i, tabContent, tabBtns;
        let month = document.getElementById("monthChart");
        let year = document.getElementById("yearChart");
        // 모든 탭 내용과 탭 버튼을 숨김
        tabContent = document.getElementsByClassName('tab-content');
        for (i = 0; i < tabContent.length; i++) {
            tabContent[i].style.display = 'none';
        }

        tabBtns = document.getElementsByClassName('tab-btn');
        for (i = 0; i < tabBtns.length; i++) {
            tabBtns[i].classList.remove('active');
        }

        // 선택된 탭을 보이게 함
        document.getElementById(tabName).style.display = 'block';

        if (tabName == 'chartMonth') {
            month.style.borderBottom = '1px solid blue';
            year.style.borderBottom = ''; // 다른 탭의 스타일 제거
            // chartMonth.render();

        } else if (tabName == 'chartYear') {
            year.style.borderBottom = '1px solid blue';
            month.style.borderBottom = ''; // 다른 탭의 스타일 제거
            // chartYear.render();
        }
    }
    // 초기에 첫 번째 탭을 열도록 함
    openTab('chartMonth');