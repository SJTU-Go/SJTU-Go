//index.js
const app = getApp()
var util = require("../../utils/util.js")
Page({
 
  /**
   * 页面的初始数据
   * selectWeek 0代表的本周  1代表下一周  -1代表上一周   
   * timeBean 传递给组件的数据，数据的格式在一开始的工具类中明确
   */
  data: {
    empty:1,
    currentday:0,
    selectWeek:0,
    timeBean:{},
    mHidden: true,
    selectShow: false,//控制下拉列表的显示隐藏，false隐藏、true显示
    selectShow2: false,//控制下拉列表的显示隐藏，false隐藏、true显示
    selectData: ['6时','7时','8时','9时','10时','11时','12时','13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时'],//下拉列表的数据
    selectData2: ['00','05','10','15','20','25','30','35','40','45','50','55'],
    showlist:new Array(),
    index: 0,//选择的下拉列表下标
    index2: 0,//选择的下拉列表下标  
    schedulename:'', 
    place:'',
    currentschedule:new Array(),
  },
 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that =this
    wx.getStorage({
      key: 'storedschedule',
      success: function(res){
      that.setData({
        currentschedule:res.data,
      })
      if(that.data. currentschedule.length==0){
      that.setData({
      status:false
      });
      }else{
      that.setData({
      status:true
      })
      }
      },
      fail: function(res) {
      console.log(res+'aaaaa')
      }
      })


  },
  addschedule:function(){
  this.setData({mHidden:false})
  },
  modalcancelled:function(){
    this.setData({mHidden:true})
    },
  changeModal:function(event){

    var result = {
        yearMonth:this.data.timeBean.yearMonth,
        selectDay:this.data.timeBean.weekDayList[parseInt(this.data.timeBean.selectDay)].day,
        timehour:this.data.index+6,
        timeminute:this.data.selectData2[this.data.index2],
        schedulename:this.data.schedulename,
        place:this.data.place};
    var ress = this.data.currentschedule;
    this.setData({mHidden:true});
    ress.push(result);
    wx.setStorage({ key:'storedschedule',
    data:ress})
    },
  /**
   * 点击了上一周，选择周数字减一，然后直接调用工具类中一个方法获取到数据
   */
  scheduleNameInput:function(e)
  {    this.setData({schedulename:e.detail.value})

  },
  placeInput:function(e)
  {    this.setData({place:e.detail.value})

  },
  lastWeek:function(e){   
    var selectWeek = --this.data.selectWeek;
    var timeBean = this.data.timeBean
    timeBean = util.getWeekDayList(selectWeek)
 
    if (selectWeek != 0) {
      timeBean.selectDay = 0;
    }
 
    this.setData({
      timeBean,
      selectWeek
    })
  },
 
  /**
   * 点击了下一周，选择周数字加一，然后直接调用工具类中一个方法获取到数据
   */
  nextWeek:function(e){
    var selectWeek = ++this.data.selectWeek;
    var timeBean = this.data.timeBean
    timeBean = util.getWeekDayList(selectWeek)
 
    if (selectWeek != 0){
      timeBean.selectDay = 0;
    }
 
    this.setData({
      timeBean,
      selectWeek
    })
  },
 
  /**
   * 选中了某一日，改变selectDay为选中日
   */ 
  dayClick:function(e){
    var timeBean = this.data.timeBean
    timeBean.selectDay = e.detail;
    this.setData({
      timeBean,
    })
    console.log(this.data.timeBean)
    this.setData({currentday:this.data.timeBean.weekDayList[parseInt(this.data.timeBean.selectDay)].day,})
    var currentdayy =  this.data.currentday ;
    var empty = this.data.currentschedule.every(function(value, index, array){
      return value.selectDay != currentdayy;})
    this.setData({empty:empty})
    var showlist = this.data.currentschedule.filter(e=>e.selectDay == currentdayy)
    this.setData({showlist: showlist})
    console.log(this.data.showlist)
  },
 
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.setData({
      timeBean: util.getWeekDayList(this.data.selectWeek)
    })
    this.setData({currentday:this.data.timeBean.weekDayList[parseInt(this.data.timeBean.selectDay)].day,})
  },
  selectTap() {
    this.setData({
      selectShow: !this.data.selectShow
    });
  },
  selectTap2() {
    this.setData({
      selectShow2: !this.data.selectShow2
    });
  },
  // 点击下拉列表
  optionTap(e) {
    let Index = e.currentTarget.dataset.index;//获取点击的下拉列表的下标
    this.setData({
      index: Index,
      selectShow: !this.data.selectShow
    });
  },
  optionTap2(e) {
    let Index2 = e.currentTarget.dataset.index;//获取点击的下拉列表的下标
    this.setData({
      index2: Index2,
      selectShow2: !this.data.selectShow2
    });
  }
 
})
