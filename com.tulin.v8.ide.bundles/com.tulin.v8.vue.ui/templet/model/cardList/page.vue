<template>
  <div>
    <page-header
      title="基础列表"
      describe="标准的列表，包含增删改查等基础操作."
    ></page-header>
    <page-layout>
      <a-card>
        <a-row :gutter="[15, 15]">
          <template v-for="(item, index) in carListDatas">
          <a-col :span="6">
            <a-card hoverable>
              <template v-slot:cover>
                <img
                  alt="example"
                  :src="item[PreviewImage]"
                />
              </template>
              <template class="ant-card-actions" v-slot:actions>
                <setting-outlined key="setting" />
                <edit-outlined key="edit" />
                <ellipsis-outlined key="ellipsis" />
              </template>
              <a-card-meta
                :title="item[Title]"
                :description="item[Description]"
              >
                <template v-slot:avatar>
                  <a-avatar
                    :src="item[SmallIcon]"
                  />
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
          </template>
        </a-row>
      </a-card>
    </page-layout>
    <page-footer></page-footer>
  </div>
</template>
<script>
import {
  SettingOutlined,
  EditOutlined,
  EllipsisOutlined,
} from "@ant-design/icons-vue";
import {queryDataList} from "/src/api/module/common";
export default {
  components: {
    SettingOutlined,
    EditOutlined,
    EllipsisOutlined,
  },
  data() {
  	const carListDatas = [];
  	
  	queryDataList({
      tableName: "${tableName}",
      keyField: "${keyField}",
      dataOrder: "${dataOrder}",
      searchValue: "",
      columns: [],
      pagination: {pageSize: 10, pageNum: 1}
    }).then(res => {
      carListDatas.value = res.data.data;
    });
    
    return {
      carListDatas,
      Title: "${Title}",
      Description: "${Description}",
      PreviewImage: "${PreviewImage}",
      SmallIcon: "${SmallIcon}"
    }
  }
};
</script>