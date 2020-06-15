// pages/modifytr/modifytr.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentdata:new Array(),
    twoVertex:[],
    markers: [
      {
        iconPath: "/images/showres1.png",
        id: 0,
        latitude: 31.025940,//31.029236,
        longitude: 121.437600,//121.452591,
        width: 1,
        height: 1,
      }
  
      ],
      hasmarkers:false,
    center:[121.437600,31.025940],
    cuTripDetail:{},
    polyline:[{color: "#FFCC33",points:[{longitude: 121.431054957, latitude: 31.0194978596},{longitude: 121.43106347, latitude: 31.0196696911}],width: 4}]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  modifytraffic: function () {
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
             
             }, 2000);
             }
            })
        }
        else{wx.navigateTo({url: '../settr/settr', })}}})
    
    
  },
  showModal: function (e) {
    var that=this
    
    wx.getStorage({
      key: 'twoVertex',
      success:function(res){
        console.log(e.markerId)

    console.log(that.data)
        console.log(res.data)
        var two=res.data
        if(two.length<2 ){
          var i=1
          for (var b =0;b<two.length;++b){
            if (two[b]==e.markerId){
              i=0
            break}
          }
          console.log(two,i)
          if (i!=0){
          two.push(e.markerId)
          console.log(two)}
        }
        else{
          var i=1
          for (var b =0;b<two.length;++b){
            if (two[b]==e.markerId){
              i=0
            break}
          }
          console.log(two,i)
          if (i!=0){
          two.shift()
          two.push(e.markerId)
          console.log(two)}  
        }
        if(two.length==2){
          var pol=that.data.polyline
         var nn={color: "#6D003A",points:[{longitude: 121.431054957, latitude: 31.0194978596},{longitude: 121.43106347, latitude: 31.0196696911}],width: 10} 
         var llll=new Array()
    
        
        wx.getStorage({
          key: 'markers',
          success:function(res1){
            console.log(res1.data) 
            var start=0
            var end=0
            if(two[0]<two[1]) {
              start=two[0]
              end=two[1]
            }
            else{
              start=two[1]
              end=two[0]
            }
            
            for(var i=start;i<=end;++i){
              var qq={longitude: 121.431054957, latitude: 31.0194978596}
              var lon=Number(res1.data[i].longitude)
              var lat=Number(res1.data[i].latitude)
              qq.longitude=lon
              qq.latitude=lat
              llll.push(qq)
            }
            nn.points=llll
            pol[1]=nn
            that.setData({ polyline: pol });
            console.log(that.data)
          }
      })      
    }
    console.log(two)
        wx.setStorage({
          data: two,
          key: 'twoVertex',
        })
  }        
        
      
    })
    this.setData({currentdata:e.markerId})
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏弹框
  hideModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  },
  onLoad: function (options) {
    wx.setStorage({
      data: [],
      key: 'twoVertex',
    })
    var that=this
    console.log(that.data)
    wx.getStorage({
      key: 'cuTripDetail',
      success:function(res){
        console.log(res.data)
        var ll=res.data
        that.setData({cuTripDetail:ll})
        
        console.log(that.data)
        var poly=new Array()
        var amarkers=new Array();
        var q = 0
        var pList =new Array()
    
      var line= new Object()
      line.color="#FFCC33"
      var co=new Array()
      wx.setStorage({
        data: ll.strategy.routeplan[1].passingVertex,
        key: 'verlist',
      })
      for (var n=0;n<ll.strategy.routeplan[1].routePath.coordinates.length;++n){
        
        var lon=ll.strategy.routeplan[1].routePath.coordinates[n][0]
        
        var lat=ll.strategy.routeplan[1].routePath.coordinates[n][1]
        var marker ={iconPath: "/images/showres1.PNG",
        id: 0,
        vertexid:0,
        latitude: 31.025940,//31.029236,
        longitude: 121.437600,//121.452591,
        width: 8,
        height: 8,}
        marker.longitude=lon
        marker.latitude=lat
        marker.id=q
        marker.vertexid=ll.strategy.routeplan[1].passingVertex[q]
        q=q+1
        amarkers.push(marker)
        var coo= new Object()
        coo.longitude=lon
        coo.latitude=lat
        co.push(coo)
      }
      line.points=co
      line.width= 4
      poly.push(line)
    
    that.setData({polyline:poly})
    that.setData({markers:amarkers})
    wx.setStorage({
      data: amarkers,
      key: 'markers',
    })
      that.setData({hasmarkers:true})
  }})
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