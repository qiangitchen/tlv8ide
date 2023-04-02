<template>
  <div>
    <page-header
      title="图文列表"
      describe="以卡片的形式展现商品或图片信息"
    ></page-header>
    <page-layout>
      <a-card>
        <a-list
          item-layout="vertical"
          size="large"
          :pagination="pagination"
          :data-source="listData"
        >
          <template v-slot:footer>
            <div><b>ant design vue</b> footer part</div>
          </template>
          <template v-slot:renderItem="{ item }">
            <a-list-item key="item[title]">
              <template v-slot:actions>
                <span v-for="{ type, text } in actions" :key="type">
                  <component v-bind:is="type" style="margin-right: 8px" />
                  {{ text }}
                </span>
              </template>
              <template v-slot:extra>
                <img
                  width="272"
                  alt="logo"
                  :src="item[previewImage]"
                />
              </template>
              <a-list-item-meta :description="item[description]">
                <template v-slot:title>
                  <a href="javascript:void(0);">{{ item[title] }}</a>
                </template>
                <template v-slot:avatar
                  ><a-avatar :src="item[smallIcon]"
                /></template>
              </a-list-item-meta>
              {{ item[content] }}
            </a-list-item>
          </template>
        </a-list>
      </a-card>
    </page-layout>
    <page-footer></page-footer>
  </div>
</template>
<script>
import {
  StarOutlined,
  LikeOutlined,
  MessageOutlined,
} from "@ant-design/icons-vue";
import {queryDataList} from "/src/api/module/common";

export default {
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  data() {
    const listData = [];
    queryDataList({
      tableName: "${tableName}",
      keyField: "${keyField}",
      dataOrder: "${dataOrder}",
      searchValue: "",
      columns: [],
      pagination: {pageSize: 3, pageNum: 1}
    }).then(res => {
      listData.value = res.data.data;
    });
    return {
      listData,
      pagination: {
        onChange: (page) => {
          console.log(page);
          loadData(page);
        },
        pageSize: 3,
      },
      actions: [
        { type: "StarOutlined", text: "156" },
        { type: "LikeOutlined", text: "156" },
        { type: "MessageOutlined", text: "2" },
      ],
      title: "${title}",
      description: "${description}",
      content: "${content}",
      previewImage: "${previewImage}",
      smallIcon: "${smallIcon}",
    };
  },
  methods: {
  	loadData(param) {
  		const that = this;
  		queryDataList({
	      tableName: "${tableName}",
	      keyField: "${keyField}",
	      dataOrder: "${dataOrder}",
	      searchValue: "",
	      columns: [],
	      pagination: {pageSize: param.pageSize, pageNum: param.pageNum}
	    }).then(res => {
	      that.listData = res.data.data;
	    });
  	}
  }
};
</script>
<style></style>