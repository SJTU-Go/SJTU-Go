// pages/modifyPark/modifyPark.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    step :0,
    history:[],
    temp:[],
    bus:{},
    walk:{},
    pass:'',
    depart:'',
    arrive:'',
    parkid: 0,
    parkname:'',
    info:'',
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preference1:['步行','共享单车','校园巴士'],
    preferencelist:[],
    banned:[],
    preferencelist: new Array(),
    searchtxt:'',
    datares: new Array(),
    markers: [
    {
      iconPath: "/mark/7.PNG",
      id: 0,
      parkid: 0,
      parkname:'',
      latitude: 31.020502,//31.029236,
      longitude: 121.434009,//121.452591,
      width: 50,
      height: 50,
    }

    ],
    currentdata:new Array(),
    hasmarkers:false
  },
  formSubmit: function (e) {
    var that= this
    //  wx.navigateTo({ url: '../commentmap/commentmap?RT='+JSON.stringify(this.data.polyline),})
    
    console.log(e)
    var mod={
      type:'park',
      name:this.data.parkname,
      id:this.data.parkid,
      formerInfo:this.data.info,
      newInfo:e.detail.value.textarea
    }
    console.log(mod)
    
    
    wx.getStorage({
      key: 'history',
     success:function(res){
      var i 
      if(res.data.hia){
      that.setData({temp:res.data.hia})
      } 
      console.log(that.data)
      var newhis=new Array();
      for (i in that.data.temp){
        console.log(that.data.temp[i])
        if(that.data.temp[i].id){
      newhis.push(that.data.temp[i])
      console.log(newhis) 
    }}
     console.log(newhis) 
     newhis.push(mod)
     console.log(newhis)
    wx.setStorage({
      data:{hia: newhis,},
        
      key: 'history',
    })  }})
    
    wx.showToast({
       title: '保存成功',
       icon: 'success',
       duration: 2000,
       success: function () {
       setTimeout(function () {
       wx.reLaunch({
       url: '../index/index',
       })
       }, 2000);
       }
      })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    wx.getStorage({
      key: 'parkid',
     success:function(res){if(res.data.name){that.setData({parkid:res.data.name})}} })
     wx.getStorage({
      key: 'parkname',
     success:function(res){if(res.data.na){that.setData({parkname:res.data.na})}} })
     wx.getStorage({
      key: 'info',
     success:function(res){if(res.data.nn){that.setData({info :res.data.nn})}} })
    
    

    
    
    console.log(this.data)
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