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
    adminID:1,
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
      key: 'admnID',
      success:function(res){
        console.log(res.data)
        var ll=res.data
        that.setData({adminID:ll})}})
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
       adminID:that.data.adminID,
       feedbackID:that.data.cuFB
     }),
  
      success (res){
        console.log(res)
        var tripid= res.data.tripID
                        var u="https://api.ltzhou.com/trip/get/id=".concat(tripid)
          wx.request({
            url: u,
            method:'GET',
            header: {
            'content-type': 'application/json'
            },
           //data:{
             //id:that.data.cuFeedback.tripID
           //},
        
            success (res1){
              console.log(res1)
              var from=res1.data.strategy.depart
              var to=res1.data.strategy.arrive
              var type=res1.data.strategy.type
              var p1=res1.data.strategy.routeplan[0].departName
              var p2=res1.data.strategy.routeplan[0].arriveName
              var t1=parseInt(res1.data.strategy.routeplan[0].routeTime)
              var tm1=''
              if (t1>=60){
                var ff= parseInt(t1/60)
                var tt=t1%60
                if(tt==0){
                  tm1=ff.toString()+"分钟"  
                }
                else{tm1=ff.toString()+"分"+tt.toString()+"秒"}
              }
              else{
                tm1=t1.toString()+"秒"
              }
              var p3=res1.data.strategy.routeplan[1].departName
              var p4=res1.data.strategy.routeplan[1].arriveName
              var t2=parseInt(res1.data.strategy.routeplan[1].routeTime)
              var tm2=''
              if (t2>=60){
                var ff= parseInt(t2/60)
                var tt=t2%60
                if(tt==0){
                  tm2=ff.toString()+"分钟"  
                }
                else{tm2=ff.toString()+"分"+tt.toString()+"秒"}
              }
              else{
                tm2=t2.toString()+"秒"
              }
              var p5=res1.data.strategy.routeplan[2].departName
              var p6=res1.data.strategy.routeplan[2].arriveName
              var t3=parseInt(res1.data.strategy.routeplan[2].routeTime)
              var tm3=''
              if (t3>=60){
                var ff= parseInt(t3/60)
                var tt=t3%60
                if(tt==0){
                  tm3=ff.toString()+"分钟"  
                }
                else{tm3=ff.toString()+"分"+tt.toString()+"秒"}
              }
              else{
                tm3=t3.toString()+"秒"
              }
              var t4=t1+t2+t3
              var tm4=''
              if (t4>=60){
                var ff= parseInt(t4/60)
                var tt=t4%60
                if(tt==0){
                  tm4=ff.toString()+"分钟"  
                }
                else{tm4=ff.toString()+"分"+tt.toString()+"秒"}
              }
              else{
                tm4=t4.toString()+"秒"
              }
                        var mm=res.data
                        mm.depart=from
                        mm.arrive=to
                        mm.d1=p1
                        mm.a1=p2
                        mm.time1=tm1
                        mm.d2=p3
                        mm.a2=p4
                        mm.time2=tm2
                        mm.d3=p5
                        mm.a3=p6
                        mm.time3=tm3
                        mm.time4=tm4
                        mm.type=type
        that.setData({cuFeedback:mm})
        
      }
    
    })
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