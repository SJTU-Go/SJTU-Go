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
    selectWeek:0,
    timeBean:{},
    mHidden: true,
    selectShow: false,//控制下拉列表的显示隐藏，false隐藏、true显示
    selectShow2: false,//控制下拉列表的显示隐藏，false隐藏、true显示
    selectData: ['0时','1时','2时','3时','4时','5时','6时','7时','8','9'],//下拉列表的数据
    selectData2: ['0fen','1fen','2时','3时','4时','5时','6时','7时','8','9'],
    index: 0,//选择的下拉列表下标
    index2: 0,//选择的下拉列表下标    
  },
 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
 
  },
  addschedule:function(){
  this.setData({mHidden:false})
  },
  modalcancelled:function(){
    this.setData({mHidden:true})
    },
  changeModal:function(){
    console.log(this.data.index)
    console.log(this.data.index2) 
    console.log(this.data.timeBean)   
    this.setData({mHidden:true})
  },
  /**
   * 点击了上一周，选择周数字减一，然后直接调用工具类中一个方法获取到数据
   */
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
  },
 
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.setData({
      timeBean: util.getWeekDayList(this.data.selectWeek)
    })
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
