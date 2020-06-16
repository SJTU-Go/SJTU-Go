// pages/history/history.js
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
    admin:'',
    adminID:1,
    custate:0,
    hislist:{}

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
    wx.getStorage({
      key: 'adminID',
      success:function(res){
        console.log(res.data)
        that.setData({adminID:res.data})
        
      
    wx.request({
      url: 'https://api.ltzhou.com/modification/view/map',
      method:'POST',
      header: {
      'content-type': 'application/x-www-form-urlencoded'
      },
     data:json2Form({
       adminID:that.data.adminID
     }),
  
     success (res){
      console.log(res)
      var tr=new Array()
      var cccc=0
      if(res.data.length==0){
        that.setData({custate:1})
      }
      else{
        that.setData({custate:0})
      }
      for( var i=res.data.length-1;i>=0;--i){
        tr[cccc]=res.data[i]
        cccc+=1
      }
      that.setData({hislist :tr})
      
    }
       
    })
  }
})
    
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