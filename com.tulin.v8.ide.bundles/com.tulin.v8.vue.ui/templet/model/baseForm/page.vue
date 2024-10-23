<template>
  <div>
    <page-header
      title="基础表单"
      describe="表单页用于向用户收集或验证信息，基础表单常见于数据项较少的表单场景。表单域标签也可支持响应式."
    ></page-header>
    <page-layout>
      <a-card>
        <a-row type="flex" justify="center">
          <a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="22" :xxl="19">
            <a-form
              ref="baseForm"
              :model="form"
              :rules="rules"
              :label-col="labelCol"
              :wrapper-col="wrapperCol"
            >
              ${formInfo}
              <a-button type="primary" @click="onSubmit">提交</a-button>
              <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
            </a-form>
          </a-col>
        </a-row>
      </a-card>
    </page-layout>
    <page-footer></page-footer>
  </div>
</template>
<script>
import {queryData, saveData} from "/src/api/module/common";
import {message, Modal} from "ant-design-vue";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";

export default {
  data() {
    return {
      labelCol: {xs: 4, sm: 3, md: 3, lg: 3, xl: 2, xxl: 3},
      wrapperCol: {xs: 20, sm: 21, md: 21, lg: 21, xl: 20, xxl: 17},
      other: "",
      form: ${formColumns},
      rules: {}
    };
  },
  methods: {
    onSubmit() {
      this.$refs.baseForm
        .validate()
        .then(() => {
          console.log("values", this.form);
          saveData({
            tableName: "${tableName}",
            keyField: "${keyField}",
            keyValue: this.form.${keyField},
            data: this.form
          }).then(res => {
            if (res.code === 200) {
              this.form.${keyField} = res.data;
              message.success(res.msg);
            } else {
              message.error(res.msg);
            }
          });
        })
        .catch(error => {
          console.log("error", error);
        });
    },
    reloadForm() {
      queryData({
        tableName: "${tableName}",
        keyField: "${keyField}",
        keyValue: '需要查询的主键',
        data: this.form
      }).then(res => {
        if (res.data) {
          this.form = res.data;
        }
      });
    },
    resetForm() {
      this.$refs.baseForm.resetFields();
    }
  }
};
</script>
<style scoped>
.ant-form-item-label {
  width: 100px;
}
</style>
