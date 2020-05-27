//index.js
const app = getApp()
Page({
  data:{
    orderList:[],
    routeList:[],
    planList:[]
  },

  onLoad:function(options){
    var that=this
   //var list=new Array()
    // 页面初始化 options为页面跳转所带来的参数
    wx.getStorage({
      key: 'history',
      success:function(res){
        console.log(res.data)
        that.setData({orderList:res.data})
      }
    })
    wx.getStorage({
      key: 'historyroute',
      success:function(res){
        console.log(res.data)
        that.setData({routeList:res.data})
      }
    })
    wx.getStorage({
      key: 'plan',
      success:function(res){
        console.log(res.data)
        that.setData({planList:res.data})
      }
    })
    //console.log(that.data.orderList)

    //this.setData({
      //  orderList:[{
      //   arrive:'菁菁堂',
      //   depart:'D24宿舍楼',
      //   passPlaces:'第四餐饮大楼',
      //   routetime:'20min',
      //  },{
      //   arrive:'霍英东体育馆',
      //   depart:'上院',
      //   passPlaces:'无',
      //   routetime:'13min',
      //  }]
   // })
  },

  searchPage:function(e){
    console.log(e)
    var index=e.currentTarget.dataset.index
    console.log(this.data.routeList)
    var route = this.data.routeList[index]
    //var route = 'routeList['+index+']'
    console.log(route)
   wx.navigateTo({ url: '../feedback/feedback?RT='+JSON.stringify(route),})



},
indexback:function()
{
  wx.switchTab(
    {
  url: '../index/index'}
  )},

  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  },
  showRoute:function(e){
    console.log(e)
    var index=e.currentTarget.dataset.index
    //console.log(this.data.routeList)
    var route = this.data.routeList[index]
    var plan = this.data.planList[index]
    //var route = 'routeList['+index+']'
    console.log(route)
    console.log(plan)
   wx.navigateTo({ url: '../commentmap/commentmap?RT='+JSON.stringify(route)+'&plan='+JSON.stringify(plan),})
  },


})

