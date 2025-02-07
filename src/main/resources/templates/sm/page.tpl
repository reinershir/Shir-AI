<template>
  <div class="app-container">
    <div class="filter-container">
<#if fieldInfos??>
    <#assign flag = "false" />
	<#list fieldInfos as item>
    <#if item.operation ??>
      <#assign flag = "true" />
	  <#if item.javaType == 'String'>
	<el-input 
	  <#elseif item.javaType == 'Date'>
	<el-date-picker type="date"
	  <#elseif item.javaType == 'Float' || item.javaType == 'Double'>
	<el-input-number :precision="2" :step="0.1"
	  <#else>
	<el-input 
	  </#if> 	  v-model="listQuery.${item.name}" :placeholder="$t('common.hint.input') + '${(item.comment ?? && item.comment != '')? then(item.comment,item.name)}'" style="width: 15%; margin-right: 50px;" />
    </#if>
	</#list>
	<#if flag == 'true'>
	<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t("common.button.search") }}
    </el-button>
    </#if>
    <br>
</#if>
      <el-button
        class="filter-item"
        style="margin:20px 0 20px 0;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate()"
      >
        {{ $t("common.button.create") }}
      </el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
<#if fieldInfos??>
	<#list fieldInfos as item>
    <#if item.isPrimaryKey>
	  <el-table-column v-if="false" align="center" label="${(item.comment ?? && item.comment != '')? then(item.comment,item.name)}">
	        <template slot-scope="scope">
	          <span>{{ scope.row.${item.name} }}</span>
	        </template>
	  </el-table-column>
  	<#else>
	  <el-table-column align="center" label="${(item.comment ?? && item.comment != '')? then(item.comment,item.name)}">
	      <template slot-scope="scope">
	        <span>{{ scope.row.${item.name} }}</span>
	      </template>
	  </el-table-column>
    </#if>
	</#list>
</#if>
      <el-table-column align="center" :label="$t('common.button.operation')" lign="center" width="260" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button class="filter-item" type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
            {{ $t('common.button.edit') }}
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(row, $index)">
            {{ $t('common.button.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="info"
        label-position="left"
        label-width="150px"
        style="margin-left:50px;"
      >
<#if fieldInfos??>
	<#list fieldInfos as item>
    <#if item.isPrimaryKey>
<el-input v-show="false" v-model="info.${item.name}" />
  	<#else>
  	<el-form-item prop="${item.name}" label="${(item.comment ?? && item.comment != '')? then(item.comment,item.name)}">
          <el-input v-model="info.${item.name}" />
        </el-form-item>
    </#if>
	</#list>
</#if>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('common.button.cancel') }}
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          {{ $t('common.button.save') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, create, update, deleteById } from '@/api/${ClassName?uncap_first}'
import Pagination from '@/components/Pagination'

export default {
  name: '${ClassName?uncap_first}List',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        pageSize: 20,
<#if fieldInfos??>
    <#assign flag = "false" />
	<#list fieldInfos as item>
	    ${item.name}: null,
	</#list>
	<#if flag == 'true'>
	<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t("common.button.search") }}
    </el-button>
    </#if>
    <br>
</#if>
      },
      dialogFormVisible: false,
      dialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: this.$t('common.button.edit'),
        create: this.$t('common.button.create')
      },
      info: {
  	<#if fieldInfos??>
	<#list fieldInfos as item>
        ${item.name}: null<#if item_has_next>,</#if>
	</#list>
	</#if>
      },
      rules: {
      <#if fieldInfos??>
	  <#list fieldInfos as item>
	    <#if item.isNull??>
	      ${item.name}: [{ required: true, message: this.$t('common.hint.input') + '${item.comment ?? ? then(item.name, item.comment)}' }]<#if item_has_next>,</#if>
	    </#if>
	  </#list>
	  </#if>
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.info = {
      }
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      this.resetTemp()
    },
    handleUpdate(row) {
      this.info = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.showPassword = false
	  this.$nextTick(() => {
	    this.$refs['dataForm'].clearValidate()
	  })
    },
    handleDelete(row, index) {
      this.$confirm(this.$t('common.hint.confirm'), 'Hint', {
        confirmButtonText: this.$t('common.button.submit'),
        cancelButtonText: this.$t('common.button.cancel'),
        type: 'warning'
      }).then(() => {
        deleteById(row.id).then(response => {
          this.$notify({
            title: 'Success',
            message: response.message,
            type: 'success',
            duration: 2000
          })
          this.list.splice(index, 1)
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          create(this.info).then(() => {
            this.list.unshift(this.info)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: this.$t('common.hint.success'),
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const listData = JSON.stringify(this.info)
          update(listData).then((response) => {
            const index = this.list.findIndex(v => v.id === this.info.id)
            this.list.splice(index, 1, this.info)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: response.message,
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}

.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>