//index.js
const app = getApp()

Page({

  data:{
    step :0,
    bus:{},
    walk:{},
    pass:'',
    depart:'',
    arrive:'',
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preferencelist: new Array(),
    searchtxt:'',
    datares: new Array(),
  }  ,
    onLoad:function(){
    var that = this

    that.setData({ 
         pass:'',
    depart:'',
    arrive:'',})},
    pass:function(e){
      this.setData({
        pass: e.detail.value,
      })
    },
    depart:function(e){
      this.setData({
        depart: e.detail.value,
      })
    },
    arrive:function(e){
      this.setData({
        arrive: e.detail.value,
      })
    },
    indexback: function(e)
    {wx.switchTab({
      url: '../index/index',})
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
                url: '../searcha/searcha?RT='+JSON.stringify(that.data.datares),
                //success:function(res){that.setData({step:0})}
              
              },

                )

            }}
              )             
    }})}},
  navigatePage:function()
  {    this.setData({step:1})
  },
  searchInput:function(e)
  {    app.globalData.search =e.detail.value
    
  },
  search:function()
  {    
  }
})
