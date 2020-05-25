//index.js
const app = getApp()
Page({
  data:{
    pass:'',
    passid:'',
    depart:'',
    departid:'',
    arrive:'',
    arriveid:'',
    currentData : 0,
    value : new Array(),
    method:["步行","校园巴士","共享单车"],
    preference:["步行","校园巴士","共享单车"],
    preferencelist: new Array(),
    routeplan:new Array(),
    arrive:'',
    pass:'',
    depart:'',
  },
  onLoad:function(options){   
    var that = this
    that.setData({
      preferencelist: JSON.parse(options.RT)[0],
      routeplan:JSON.parse(options.RT)[2],
    }
    )

    var arr = JSON.parse(options.RT)[1]
    var compare = function (obj1, obj2) {
      var val1 = obj1.travelTime;
      var val2 = obj2.travelTime;
      if (val1 < val2) {
          return -1;
      } else if (val1 > val2) {
          return 1;
      } else {
          return 0;
      }            
  } 

  this.setData({value:arr.sort(compare)})


wx.getStorage({
  key: 'depart',
success:function(res){
  that.setData({depart:res.data.name,departid : 'DT'+res.data.id})


} })
wx.getStorage({
  key: 'pass',
success:function(res){if(res.data.name){that.setData({pass:res.data.name,passid :'DT'+res.data.id})}} })
wx.getStorage({
  key: 'arrive',
success:function(res){that.setData({arrive:res.data.name,arriveid :'DT'+res.data.id})} })


  },

  pass:function(){
    console.log("pass")
    wx.navigateTo({
      url: '../extendsearcha/pass/pass',
    })
        },
        depart:function(e){
          wx.navigateTo({
            url: '../extendsearcha/depart/depart',
          })
        },
        arrive:function(e){
          wx.navigateTo({
            url: '../extendsearcha/arrive/arrive',
          })
        },

  searchPagebus: function()
  {
    wx.navigateTo({
      url: '../search/search?RT='+JSON.stringify(this.data.routeplan),})
    
  },
  checkCurrent:function(e){
    const that = this;
 
    if (that.data.currentData === e.target.dataset.current){
        return false;
    }else{
 
      that.setData({
        currentData: e.target.dataset.current
      })
    }},
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  formSubmit: function (e) {
    if (!this.data.depart|!this.data.arrive){        wx.showToast({ 
      title: '输入错误', 
      icon: 'loading', 
      duration: 2000 
      }) }
    else
    {
    var depart=String( this.data.depart)
    var arrive=String( this.data.arrive)
    var pass=this.data.pass
    var passlist=[];
    if(pass){
    passlist.push(pass)};
    var that =this;
    var tem;
    var valuetem=new Array();
    var pre = new Array();
    var i;
    var j = 0;
    var preres = new Array();
    console.log({
      "arrivePlace": arrive,
      "beginPlace": depart,
      "departTime": "2020/05/11 12:05:12",
      "passPlaces": passlist,})
    wx.request({
      url: 'https://api.ltzhou.com/navigate/bus',
      method:'POST',
      header: {
      'content-type': 'application/json'
      },
      data:{
      "arrivePlace": arrive,
      "beginPlace": depart,
      "departTime": "2020/05/11 12:05:12",
      "passPlaces": passlist,},

      success (res) {
        tem = res.data
        console.log(tem)
        that.setData({bus:tem})
        valuetem.push(tem)
        wx.request({
          url: 'https://api.ltzhou.com/navigate/walk',
          method:'POST',
          header: {
          'content-type': 'application/json'},
        data:{
          "arrivePlace": arrive,
          "beginPlace": depart,
          "passPlaces": passlist,
          },
          success (res) {
            tem = res.data
            console.log(tem)
            valuetem.push(tem)
            that.setData({value:valuetem})
            console.log(that.data.value)
            console.log("1")
            for(j=0;j<that.data.preference.length;j++){
              for (i=0;i<that.data.value.length;i++){
                if(that.data.value[i].type==that.data.preference[j]){pre.push(i)}}}
            for (i=0;i<pre.length;i++){preres.push(that.data.value[pre[i]])}
            console.log(preres)
            var ressss = new Array()
            ressss.push(preres)
            ressss.push(valuetem)
            ressss.push(that.data.bus.routeplan)
            ressss.push(depart)
            ressss.push(pass)
            ressss.push(arrive) 
            that.setData({datares:ressss})
            console.log(that.data.datares)
            wx.navigateTo({
              url: '../searcha/searcha?RT='+JSON.stringify(that.data.datares),})
          }}
            )             
  }})}},
indexback:function()
{    this.setData({step:1})
wx.switchTab({
  url: '../index/index',})
}, 

  onUnload:function(){
    // 页面关闭
  }

})

