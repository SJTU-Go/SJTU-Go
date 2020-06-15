// pages/settr/settr.js
var dateTimePicker = require('../../utils/dateTimePicker.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isTR:0,
    
    temp:[],
    admin:1,
    date: '2020-06-08',
    time: '12:00',
    time2: '12:00',
    dateTimeArray: null,
    dateTime: null,
    dateTimeArray1: null,
    dateTime1: null,
    pointList: [
      
    ],
    
    relatedVertex: [
      174061,
      140827,
      141410,
      140826
    ],
    startYear: 2020,
    endYear: 2050
  },
  changeDate(e){
    this.setData({ date:e.detail.value});
    console.log(this.data)
  },
  changeTime(e){
    this.setData({ time: e.detail.value });
    console.log(this.data)
  },
  changeTime2(e){
    this.setData({ time2: e.detail.value });
    console.log(this.data)
  },
  formSubmit: function (e) {
    var that=this
    console.log(that.data)
    console.log(e)
    var da=that.data.date
    var dat=da.replace(/-/g, "/")
    var mod={
      adminID: 1,
      beginDay: dat,
      beginTime: that.data.time,
      bikeSpeed: e.detail.value.bikeSpeed,
      carSpeed: e.detail.value.carSpeed,
      endTime: that.data.time2,
      message: e.detail.value.message,
      motorSpeed: e.detail.value.motorSpeed,
      name: e.detail.value.name,
      
      relatedVertex: that.data.relatedVertex,
      repeatTime: e.detail.value.repeat
      
    }
    console.log(mod)
    wx.getStorage({
      key: 'traffichistory',
     success:function(res){
      that.setData({isTR:1})
      var i 
      if(res.data){
      that.setData({temp:res.data})
      } 
      console.log(that.data)
      var newhis=new Array();
      for (i in that.data.temp){
        console.log(that.data.temp[i])
        if(that.data.temp[i].name){
      newhis.push(that.data.temp[i])
      console.log(newhis) 
    }}
     console.log(newhis) 
     newhis.push(mod)
     console.log(newhis)
     wx.setStorage({
      data: newhis,
        
      key: 'traffichistory',
    })
      }})
      if (that.data.isTR==0){
        var newhis=new Array()
      newhis.push(mod)
        wx.setStorage({
          data: newhis,
            
          key: 'traffichistory',
        })
      }
    //  wx.navigateTo({ url: '../commentmap/commentmap?RT='+JSON.stringify(this.data.polyline),})
    wx.showToast({
       title: '修改已保存',
       icon: 'success',
       duration: 2000,
       success: function () {
       setTimeout(function () {
       wx.reLaunch({
       url: '../messagePage/messagePage',
       })
       }, 2000);
       }
      })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this
    wx.getStorage({
      key: 'admin',
      success:function(res){
        console.log(res.data)
        that.setData({admin:res.data})
        
      }
    })
    var llll=new Array()
    wx.getStorage({
      key: 'twoVertex',
      success:function(res){
        console.log(res.data)
        var t=res.data
        if (t.length!=2){
          wx.showToast({
             title: '节点数不足',
             icon: 'loading',
             duration: 1000,
             success: function () {
             setTimeout(function () {
             wx.navigateTo({
             url: '../modifytr/modifytr',
             })
             }, 2000);
             }
            })
        }
        wx.getStorage({
          key: 'markers',
          success:function(res1){
            console.log(res1.data) 
            var start=0
            var end=0
            if(t[0]<t[1]) {
              start=t[0]
              end=t[1]
            }
            else{
              start=t[1]
              end=t[0]
            }
            
            for(var i=start;i<=end;++i){
              console.log(i,res1.data[i],res1.data[i].vertexid)
              var location=Number(res1.data[i].vertexid)
              llll.push(location)
            }
            that.setData({ relatedVertex: llll });
            console.log(that.data)
          }
        })
        
        
        
      }
      
    })
    console.log(that.data)
    console.log(1+1)
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