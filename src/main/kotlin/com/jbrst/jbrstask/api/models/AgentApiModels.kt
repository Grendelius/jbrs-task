package com.jbrst.jbrstask.api.models


data class AgentsDto(val count: Int, val agent: List<AgentDto>, val href: String)

data class AgentDto(val id: Int, val name: String, val typeId: Int, val href: String, val webUrl: String)