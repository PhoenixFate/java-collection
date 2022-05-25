<template>
  <div>
    修改用户信息{{ id }}, 名称：{{ name }}
  </div>
</template>

<script>
export default {
  layout: 'test',
  async asyncData() {
    console.log("请求服务端接口。。。")

    //使用async await来同步调用接口
    var a = await new Promise((resolve, reject) => {
      setTimeout(() => {
        console.log("await a")
        resolve('await a')
      }, 2000)
    })

    var b = await new Promise((resolve, reject) => {
      setTimeout(() => {
        console.log("await b")
        resolve('await b')
      })
    }, 1000)


    return {
      name: 'phoenix'
    }
  },
  data() {
    return {
      id: '',
      name: ''
    }
  },
  methods: {
    getUser: function () {
      //ajax请求服务的接口
      this.name = 'phoenix'
    },
    a() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          console.log('a')
          resolve('1')
        }, 2000)
      })
    },
    b() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          console.log('b')
          resolve('2')
        }, 1000)
      })
    }
  },
  mounted() {
    this.id = this.$route.params.id
    console.log(this.id)
    //传统的使用这种方式，但nuxt使用asyncData，在服务端生成前端页面
    //this.getUser()
    //正常两个都是异步调用
    // this.a().then((res) => {
    //   console.log('res a: ' + res)
    // })
    // this.b().then((res) => {
    //   console.log('res b: ' + res)
    // })
  }
}
</script>

<style scoped>

</style>
