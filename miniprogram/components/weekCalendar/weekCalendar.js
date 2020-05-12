
Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
 
  /** * 组件的属性列表 * 用于组件自定义设置 */
  properties: {
    timeBean: { // 属性名 在wxml调用组件时利用该属性传递组件显示的数据
      type: Object, // 类型（必填），目前接受的类型包括：String, Number, Boolean, Object, Array, null（表示任意类型） 
      value: ''// 属性初始值（可选），如果未指定则会根据类型选择一个
    },
  },
 
  /**
   * 组件的方法列表
   */
  methods: {
    lastWeek:function(e){ //点击了上一周
      this.triggerEvent("lastWeek")
    },
 
    nextWeek:function(e){ //点击了下一周
      this.triggerEvent("nextWeek")
    },
 
    itemClick: function (e) {  //点击了某一日，传递该日的下标
      var index = e.currentTarget.dataset.index
      this.triggerEvent("dayClick", index);
    },
  }
})
