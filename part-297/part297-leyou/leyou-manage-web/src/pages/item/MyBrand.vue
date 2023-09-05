<template>
    <div>
      <v-layout class="px-2 pb-4">
        <v-flex xs2> <v-btn color="info" >新增品牌</v-btn></v-flex>
        <v-spacer></v-spacer>
        <v-flex xs4>

          <v-text-field label="搜索"  hide-details append-icon="search" v-model="searchValue">

          </v-text-field>
        </v-flex>
      </v-layout>


      <v-data-table
        :headers="headers"
        :items="brands"
        :pagination.sync="pagination"
        :total-items="totalBrands"
        class="elevation-1"
      >
        <template slot="items" slot-scope="props">
          <td class="text-xs-center">{{props.item.id}}</td>
          <td class="text-xs-center">{{props.item.name}}</td>
          <td class="text-xs-center"><img :src="props.item.image" alt=""></td>
          <td class="text-xs-center">{{props.item.letter}}</td>
          <td class="text-xs-center">
            <v-btn flat icon color="info">
              <v-icon>edit</v-icon>
            </v-btn>
            <v-btn flat icon color="error">
              <v-icon>delete</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>

    </div>
</template>
<script>
  export default {
    data () {
      return {
        totalDesserts: 0,
        desserts: [],
        brands:[],
        pagination:{

        },
        totalBrands:0,
        loading: true,
        options: {},
        searchValue:'',//搜索条件
        headers: [
          {
            text: '品牌id',
            align: 'center',
            sortable: true,
            value: 'id',
          },
          { text: '品牌名称',        align: 'center', sortable: false, value: 'name' },
          { text: '品牌图片地址',        align: 'center',sortable: false,  value: 'image' },
          { text: '品牌的首字母',        align: 'center',sortable: true, value: 'letter' },
          { text: '操作',        align: 'center',sortable: false },
        ],
      }
    },
    watch: {
      // options: {
      //   handler () {
      //     this.getDataFromApi()
      //       .then(data => {
      //         this.desserts = data.items
      //         this.totalDesserts = data.total
      //       })
      //   },
      //   deep: true,
      // },
    },
    created () {

    },
    methods: {
      loadBrands(){
        this.loading=true;
        console.log("loadBrands")
        let params={
          searchValue:this.searchValue,          //搜索字段
          page:this.pagination.page,             //当前页
          rows:this.pagination.rowsPerPage,      //每页大小
          sortBy:this.pagination.sortBy,        //排序字段
          descending:this.pagination.descending  //是否降序
        }
        console.log(params)
        this.$http.get("item/brand/page",{params:{...params}}
        ).then(res=>{
          this.loading=false;
          if(res.status===200){
            console.log(res)
            this.brands=res.data.data;
            this.totalBrands=res.data.total;
          }

        })
      },


    },
    watch:{
      searchValue(){
        this.pagination.page=1;
      },
      pagination:{
        deep:true,
        handler(){
          this.loadBrands();
        }
      }
    }
  }
</script>

<style scoped>

</style>
