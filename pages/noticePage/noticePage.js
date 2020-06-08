// pages/noticePage/noticePage.js
var dateTimePicker = require('../../utils/dateTimePicker.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    date: '2020-06-08',
    time: '12:00',
    dateTimeArray: null,
    dateTime: null,
    dateTimeArray1: null,
    dateTime1: null,
    startYear: 2020,
    endYear: 2050

  },
  formSubmit: function (e) {
    var that= this
    
    console.log(e.detail.value.textarea)
    var text=e.detail.value.textarea
    var startTime=that.data.dateTimeArray[0][that.data.dateTime[0]]+'/'+that.data.dateTimeArray[1][that.data.dateTime[1]]+'/'+that.data.dateTimeArray[2][that.data.dateTime[2]]+' '+that.data.dateTimeArray[3][that.data.dateTime[3]]+':'+that.data.dateTimeArray[4][that.data.dateTime[4]]
    var endTime=that.data.dateTimeArray1[0][that.data.dateTime1[0]]+'/'+that.data.dateTimeArray1[1][that.data.dateTime1[1]]+'/'+that.data.dateTimeArray1[2][that.data.dateTime1[2]]+' '+that.data.dateTimeArray1[3][that.data.dateTime1[3]]+':'+that.data.dateTimeArray1[4][that.data.dateTime1[4]]
    console.log(startTime,endTime)
    wx.request({

      url: 'https://api.ltzhou.com/notice/post',
      method:'POST',
      header: {
      'content-type': 'application/json'
      },
      
      data:{
        "contents": text,
        "publisherID": 1,
        "validBeginTime": startTime,
        "validEndTime": endTime
      },
  
      success (res){console.log(res)}
       
    })
    wx.showToast({
       title: '发布成功',
       icon: 'success',
       duration: 2000,
       success: function () {
       setTimeout(function () {
       wx.reLaunch({
       url: '../userInfoPage/userInfoPage',
       })
       }, 2000);
       }
      })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
// 获取完整的年月日 时分秒，以及默认显示的数组
var obj = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
var obj1 = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
// 精确到分的处理，将数组的秒去掉
obj.dateTimeArray.pop();
 obj.dateTime.pop();
 obj1.dateTimeArray.pop();
 obj1.dateTime.pop();

this.setData({
  dateTime: obj.dateTime,
  dateTimeArray: obj.dateTimeArray,
  dateTimeArray1: obj1.dateTimeArray,
  dateTime1: obj1.dateTime
});

  },
  changeDate(e){
    this.setData({ date:e.detail.value});
    console.log(this.data)
  },
  changeTime(e){
    this.setData({ time: e.detail.value });
    console.log(this.data)
  },
  changeDateTime(e){
    this.setData({ dateTime: e.detail.value });
    console.log(this.data)
    console.log(1+1)
  },
  changeDateTime1(e) {
    this.setData({ dateTime1: e.detail.value });
    console.log(this.data)
    console.log(1+2)
  },
  changeDateTimeColumn(e){
    var arr = this.data.dateTime, dateArr = this.data.dateTimeArray;

    arr[e.detail.column] = e.detail.value;
    dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);

    this.setData({
      dateTimeArray: dateArr,
      
    });
    console.log(this.data)
    console.log(1+1000)
  },
  changeDateTimeColumn1(e) {
    var arr = this.data.dateTime1, dateArr = this.data.dateTimeArray1;

    arr[e.detail.column] = e.detail.value;
    dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);

    this.setData({ 
      dateTimeArray1: dateArr,
      
    });
    console.log(this.data)
    console.log(1+2000)
  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})