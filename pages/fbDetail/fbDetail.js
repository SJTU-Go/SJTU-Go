// pages/fbDetail/fbDetail.js
function json2Form(json) { 
  var str = []; 
  for(var p in json){ 
    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(json[p])); 
  } 
  return str.join("&"); 
} 
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cuFB:0,
cuFeedback:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this
    console.log(that.data)
    wx.getStorage({
      key: 'cuFB',
      success:function(res){
        console.log(res.data)
        var ll=res.data
        that.setData({cuFB:ll})
        
        console.log(that.data)
    wx.request({
      url: 'https://api.ltzhou.com/feedback/processfeedback',
      method:'POST',
      header: {
      'content-type': 'application/x-www-form-urlencoded'
      },
     data:json2Form({
       adminID:1,
       feedbackID:that.data.cuFB
     }),
  
      success (res){
        console.log(res)
        that.setData({cuFeedback:res.data})
        
      }
       
    })
  }})},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  modifytraffic: function () {
    var that = this
    var u="https://api.ltzhou.com/trip/get/id=".concat(that.data.cuFeedback.tripID)
    wx.request({
      url: u,
      method:'GET',
      header: {
      'content-type': 'application/json'
      },
     //data:{
       //id:that.data.cuFeedback.tripID
     //},
  
      success (res){
        console.log(res)
        wx.setStorage({
          data:res.data,
            
          key: 'cuTripDetail',
        }) 
        
      }
       
    })
    
    wx.navigateTo({url: '../modifytr/modifytr', })
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