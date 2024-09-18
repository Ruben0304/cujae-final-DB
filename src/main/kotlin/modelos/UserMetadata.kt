package modelos

import kotlinx.serialization.Serializable

@Serializable
data class UserMetadata(val user_id: String,val hospital_id:String?, val rol:String, val nombre:String, val apellidos:String)
