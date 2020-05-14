//index.js
const app = getApp()
Page({
  data:{},
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
       orderList:[{
        arrive:'菁菁堂',
        depart:'D24宿舍楼',
        passPlaces:'第四餐饮大楼',
        routetime:'20min',
       },{
        arrive:'霍英东体育馆',
        depart:'上院',
        passPlaces:'无',
        routetime:'13min',
       }]
    })
  },
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
  }
})

