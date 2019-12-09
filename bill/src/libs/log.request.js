import HttpRequest from '@/libs/axios'
import config from '@/config'
const baseUrl = process.env.NODE_ENV === 'development' ? config.logUrl.dev : config.logUrl.pro

const logAxios = new HttpRequest(baseUrl)
export default logAxios
