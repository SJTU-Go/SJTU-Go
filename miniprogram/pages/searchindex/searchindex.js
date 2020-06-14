//index.js
const app = getApp()

Page({

  data:{
    checkInfo: [
      {name: 'jam',value: '避开拥堵'},
    ],
    avoidjam : false,
    step :0,
    car:{},
    bus:{},
    walk:{},
    pass:'',
    passid:'',
    depart:'',
    departid:'',
    arrive:'',
    arriveid:'',
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士","共享单车","旋风E100"],
    preference:[],
    preferencelist: new Array(),
    searchtxt:'',
    datares: new Array(),
    passnum:0
  }  ,
    onLoad:function(options){
      var that = this
      wx.getStorage({
        key: 'preference',
      success:function(res){
        that.setData({preference:res.data})

      
      } })
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
      success:function(res){
        if(res.data.id){that.setData({arrive:res.data.name,arriveid :'DT'+res.data.id})}
      
        else{that.setData({arrive:res.data.name,arriveid :res.data.name})}
      
      }

      
      })

},


    pass:function(){
wx.navigateTo({
  url: '../extendsearch/pass/pass',
})
    },
    depart:function(e){
      wx.navigateTo({
        url: '../extendsearch/depart/depart',
      })
    },
    arrive:function(e){
      wx.navigateTo({
        url: '../extendsearch/arrive/arrive',
      })
    },
    indexback: function(e)
    {wx.switchTab({
      url: '../index/index',})
    },
    formSubmit: function (e) {
      var avoidTraffic=this.data.avoidjam
      console.log(avoidTraffic)
      if (!this.data.depart|!this.data.arrive){        wx.showToast({ 
        title: '输入错误', 
        icon: 'loading', 
        duration: 2000 
        }) }
      else
      {
        var depart
          depart=String( this.data.departid)
          if (depart=="DT404"){console.log("404"),depart=this.data.depart}
        var arrive
          arrive=String( this.data.arriveid)
          if (arrive=="DT404"){console.log("404"),arrive=this.data.arrive}
        var pass
          pass=String( this.data.passid)
          if (pass=="DT404"){console.log("404"),pass=this.data.pass}
  
      var arrivename = this.data.arrive
      var departname = this.data.depart
      var passname = this.data.pass
      var passlist=[];
      console.log("传入数据")
        console.log(depart)
        console.log(arrive)
        console.log(pass)
      var that =this;
      var tem;
      var valuetem=new Array();
      var pre = new Array();
      var i;
      var j = 0;
      var preres = new Array();
      if(pass){
        passlist.push(pass)};
      console.log({
        "arrivePlace": arrive,
        "beginPlace": depart,
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": passlist,})
      
        //busrequest
  
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
        "passPlaces": passlist,
        "avoidTraffic":avoidTraffic,
      },
  
        success (res) {
          tem = res.data
          console.log(tem)
          that.setData({bus:tem})
          valuetem.push(tem)
          //walkrequest
          wx.request({
            url: 'https://api.ltzhou.com/navigate/walk',
            method:'POST',
            header: {
            'content-type': 'application/json'},
          data:{
            "arrivePlace": arrive,
            "beginPlace": depart,
            "passPlaces": passlist,
            "avoidTraffic":avoidTraffic,
            },
            success (res) {
              tem = res.data
              console.log(tem)
              valuetem.push(tem)
      wx.request({
        url: 'https://api.ltzhou.com/navigate/bike',
        method:'POST',
        header: {
        'content-type': 'application/json'},
      data:{
        "arrivePlace": arrive,
        "beginPlace": depart,
        "passPlaces": passlist,
        "avoidTraffic":avoidTraffic,
        },
        success (res) {
          tem = res.data
          console.log(tem)
          valuetem.push(tem)
          // 旋风100
          wx.request({
            url: 'https://api.ltzhou.com/navigate/car',
            method:'POST',
            header: {
            'content-type': 'application/json'},
          data:{
            "arrivePlace": arrive,
            "beginPlace": depart,
            "passPlaces": passlist,
            "avoidTraffic":avoidTraffic,
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
          ressss.push(departname)
          ressss.push(passname)
          ressss.push(arrivename)
  
  
          that.setData({datares:ressss})
          console.log("coming resuuuu")
          console.log(that.data.datares)
  
          wx.navigateTo({
            url: '../searcha/searcha?RT='+JSON.stringify(that.data.datares),
            //success:function(res){that.setData({step:0})}
          
          },
  
            )
  
        }})
        
        
        }
      })        
  
  
         
  
            }}
              )             
    }})}
  
  },
  navigatePage:function()
  {    this.setData({step:1})
  },
  searchInput:function(e)
  {    app.globalData.search =e.detail.value
    
  },
  search:function()
  {    
  },
  addpass:function()
  {this.setData({passnum:this.data.passnum+1})
  
  },
deletepass:function(){this.setData({passnum:this.data.passnum-1})},
checkboxChange: function(e) {
  //console.log(this.data.avoidjam)
  if(e.detail.value[0]=='jam')
  {console.log("jamming") 
    this.setData({avoidjam:true})}
    else{console.log("notjamming") 
      this.setData({avoidjam:false})}
     // console.log(this.data.avoidjam)
}

})
